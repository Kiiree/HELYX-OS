/*--------------------------------*- Java -*---------------------------------*\
 |		 o                                                                   |                                                                                     
 |    o     o       | HelyxOS: The Open Source GUI for OpenFOAM              |
 |   o   O   o      | Copyright (C) 2012-2016 ENGYS                          |
 |    o     o       | http://www.engys.com                                   |
 |       o          |                                                        |
 |---------------------------------------------------------------------------|
 |	 License                                                                 |
 |   This file is part of HelyxOS.                                           |
 |                                                                           |
 |   HelyxOS is free software; you can redistribute it and/or modify it      |
 |   under the terms of the GNU General Public License as published by the   |
 |   Free Software Foundation; either version 2 of the License, or (at your  |
 |   option) any later version.                                              |
 |                                                                           |
 |   HelyxOS is distributed in the hope that it will be useful, but WITHOUT  |
 |   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or   |
 |   FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License   |
 |   for more details.                                                       |
 |                                                                           |
 |   You should have received a copy of the GNU General Public License       |
 |   along with HelyxOS; if not, write to the Free Software Foundation,      |
 |   Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA            |
\*---------------------------------------------------------------------------*/


package eu.engys.gui.casesetup.materials.panels;

import static eu.engys.core.project.constant.TransportProperties.MATERIAL_NAME_KEY;
import static eu.engys.gui.casesetup.materials.panels.MaterialsDatabasePanel.COPY_OF_SUFFIX;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.inject.Inject;

import eu.engys.core.dictionary.Dictionary;
import eu.engys.core.modules.ApplicationModule;
import eu.engys.core.modules.ModulesUtil;
import eu.engys.core.project.Model;
import eu.engys.core.project.materials.Material;
import eu.engys.core.project.materials.Materials;
import eu.engys.core.project.state.State;
import eu.engys.core.project.state.StateBuilder;
import eu.engys.gui.AbstractGUIPanel;
import eu.engys.gui.casesetup.materials.CompressibleMaterialsPanel;
import eu.engys.gui.casesetup.materials.IncompressibleMaterialsPanel;
import eu.engys.gui.casesetup.materials.MaterialsTreeNodeManager;
import eu.engys.gui.tree.TreeNodeManager;
import eu.engys.util.ui.ExecUtil;
import eu.engys.util.ui.UiUtil;
import eu.engys.util.ui.builder.PanelBuilder;

public class MaterialsPanel extends AbstractGUIPanel {

    public static final String MATERIALS = "Materials";
    private static final String MATERIAL_CHANGED_WARNING = "A material has been changed.\nAll fields default settings are going to be reset now.\nContinue?";

    private JDialog dialog;

    private MaterialParametersPanel parametersPanel;

    private MaterialsTreeNodeManager treeNodeManager;
    private Material materialToChange;

    private CardLayout centerPanelLayout;
    private JPanel centerPanel;

    private JButton changeMaterialButton;

    private PanelBuilder parametersBuilder;

    private Set<ApplicationModule> modules;

    private CompressibleMaterialsPanel compressiblePanel;
    private IncompressibleMaterialsPanel incompressiblePanel;

    private MaterialsDatabasePanel materialsDatabasePanel;

    @Inject
    public MaterialsPanel(Model model, MaterialsDatabasePanel materialsDatabasePanel, CompressibleMaterialsPanel compressiblePanel, IncompressibleMaterialsPanel incompressiblePanel, Set<ApplicationModule> modules) {
        super(MATERIALS, model);
        this.materialsDatabasePanel = materialsDatabasePanel;
        this.compressiblePanel = compressiblePanel;
        this.incompressiblePanel = incompressiblePanel;
        this.modules = modules;
        this.treeNodeManager = new MaterialsTreeNodeManager(model, this);
        model.addObserver(treeNodeManager);
    }

    protected JComponent layoutComponents() {
        centerPanelLayout = new CardLayout();
        centerPanel = new JPanel(centerPanelLayout);
        centerPanel.add(new JLabel(), "none");

        parametersPanel = new MaterialParametersPanel(compressiblePanel, incompressiblePanel);

        parametersBuilder = new PanelBuilder();
        parametersBuilder.addComponent(parametersPanel);

        ModulesUtil.configureMaterialsView(modules, parametersBuilder);

        centerPanel.add(parametersBuilder.removeMargins().getPanel(), "material");

        PanelBuilder mainbuilder = new PanelBuilder();
        mainbuilder.addComponent(createButtonsPanel());
        mainbuilder.addComponent(centerPanel);

        return mainbuilder.removeMargins().getPanel();
    }

    private JComponent createButtonsPanel() {
        List<JComponent> actionsList = new ArrayList<JComponent>();
        changeMaterialButton = new JButton(new ChangeMaterialAction());
        changeMaterialButton.setName("Change Material");
        changeMaterialButton.setEnabled(false);
        actionsList.add(changeMaterialButton);

        JComponent buttonsPanel = UiUtil.getCommandRow(actionsList);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder());
        return buttonsPanel;
    }

    private void buildDialog() {
        JButton okButton = new JButton(new OkDialogAction());
        okButton.setName("OK");

        JButton cancelButton = new JButton(new CancelDialogAction());
        cancelButton.setName("Cancel");
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        dialog = new JDialog(UiUtil.getActiveWindow(), "Materials Database", JDialog.DEFAULT_MODALITY_TYPE);
        dialog.setName("materials.database.dialog");
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(null);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(materialsDatabasePanel, BorderLayout.CENTER);
        dialog.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
        dialog.getRootPane().setDefaultButton(okButton);
    }

    @Override
    public void load() {
        materialsDatabasePanel.load();
        parametersPanel.load(model);
    }

    @Override
    public void save() {
        treeNodeManager.getSelectionHandler().handleSelection(false, (Object[]) treeNodeManager.getSelectedValues());
    }

    @Override
    public void clear() {
        super.clear();
        treeNodeManager.getSelectionHandler().handleSelection(false, (Object[]) new Material[0]);
    }

    @Override
    public void stateChanged() {
        ExecUtil.invokeLater(new Runnable() {
            @Override
            public void run() {
                State state = model.getState();

                parametersPanel.show(state);
                materialsDatabasePanel.load();

                Materials materials = model.getMaterials();
                if (!materials.isEmpty()) {
                    for (Material material : materials) {
                        for (ApplicationModule module : modules) {
                            if (module.getMaterialsView() != null) {
                                module.getMaterialsView().updateDefaultMaterial(material);
                            }
                        }
                        updateSelection(material);
                        saveMaterials(material);
                    }
                }

            }
        });
    }

    public void updateSelection(Material... ms) {
        if (ms.length == 1) {
            Material material = ms[0];
            centerPanelLayout.show(centerPanel, "material");
            parametersPanel.setMaterial(material.getDictionary());
            for (ApplicationModule module : modules) {
                if (module.getMaterialsView() != null)
                    module.getMaterialsView().updateGUIFromModel(material);
            }
        } else {
            centerPanelLayout.show(centerPanel, "none");
        }

    }

    public void saveMaterials(Material... selection) {
        if (selection != null && selection.length == 1) {
            Material material = selection[0];
            Dictionary d = new Dictionary(parametersPanel.getMaterial(model));
            material.setDictionary(d);

            for (ApplicationModule module : modules) {
                if (module.getMaterialsView() != null) {
                    module.getMaterialsView().updateModelFromGUI(material);
                }
            }
        }
    }

    public void updateButtons(boolean enable) {
        changeMaterialButton.setEnabled(enable);
    }

    private final class ChangeMaterialAction extends AbstractAction {

        public ChangeMaterialAction() {
            super("Change Material");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (dialog == null) {
                        buildDialog();
                    }
                    materialToChange = treeNodeManager.getSelectedValues()[0];
                    dialog.setVisible(true);
                }
            });
        }
    }

    private final class OkDialogAction extends AbstractAction {

        public OkDialogAction() {
            super("OK");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (model.getState().getMultiphaseModel().isMultiphase()) {
                int retVal = JOptionPane.showConfirmDialog(UiUtil.getActiveWindow(), MATERIAL_CHANGED_WARNING, "Material Changed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (retVal != JOptionPane.OK_OPTION) {
                    return;
                }

            }
            materialsDatabasePanel.saveUserMaterials();
            dialog.setVisible(false);
            parametersPanel.getNameField(model).setEnabled(false);
            changeMaterial();
        }

        private void changeMaterial() {
            ExecUtil.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Material newMaterial = getNewMaterial();
                    if (model.getState().getMultiphaseModel().isMultiphase()) {
                        StateBuilder.changeMaterial(model, modules);
                    } else {
                        model.materialsChanged();
                    }
                    treeNodeManager.setSelectedValue(newMaterial);
                    save();
                }
            });
        }

        private Material getNewMaterial() {
            int index = -1;
            if (materialToChange != null) {
                index = model.getMaterials().indexOf(materialToChange);
                model.getMaterials().remove(materialToChange);
                materialToChange = null;
            }

            Dictionary dictionary = materialsDatabasePanel.getMaterial();
            
            Material material = null;

            if (modelAlreadyContainsMaterial(dictionary.getName())) {
                Dictionary newDict = new Dictionary(dictionary);
                String newName = dictionary.getName() + COPY_OF_SUFFIX;
                newDict.setName(newName);
                newDict.add(MATERIAL_NAME_KEY, newName);
                material = new Material(newName, newDict);
                dictionary = newDict;
            } else {
                material = new Material(dictionary.getName(), dictionary);
            }

            if (index != -1) {
                model.getMaterials().add(index, material);
            } else {
                model.getMaterials().add(material);
            }
            parametersPanel.setMaterial(dictionary);
            return material;
        }

        private boolean modelAlreadyContainsMaterial(String name) {
            for (Material mat : model.getMaterials()) {
                if (mat.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final class CancelDialogAction extends AbstractAction implements Runnable {

        public CancelDialogAction() {
            super("Cancel");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(this);
        }

        @Override
        public void run() {
            dialog.setVisible(false);
        }
    }

    @Override
    public TreeNodeManager getTreeNodeManager() {
        return treeNodeManager;
    }

}
