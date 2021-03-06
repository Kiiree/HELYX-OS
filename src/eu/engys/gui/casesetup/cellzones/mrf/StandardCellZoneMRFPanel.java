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


package eu.engys.gui.casesetup.cellzones.mrf;

import static eu.engys.core.project.zero.cellzones.CellZonesUtils.AXIS_KEY;
import static eu.engys.core.project.zero.cellzones.CellZonesUtils.OMEGA_KEY;
import static eu.engys.core.project.zero.cellzones.CellZonesUtils.ORIGIN_KEY;
import static eu.engys.gui.casesetup.cellzones.CellZonesFactory.mrf;
import static eu.engys.gui.casesetup.cellzones.mrf.StandardMRF.MRF_LABEL;

import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import eu.engys.core.dictionary.Dictionary;
import eu.engys.core.dictionary.model.DictionaryModel;
import eu.engys.core.modules.cellzones.CellZonePanel;
import eu.engys.core.project.zero.cellzones.CellZoneType;
import eu.engys.util.ui.builder.PanelBuilder;

public class StandardCellZoneMRFPanel implements CellZonePanel {

    private PanelBuilder builder = new PanelBuilder();
    private DictionaryModel mrfModel;

    @Inject
    public StandardCellZoneMRFPanel() {
    }

    @Override
    public void layoutPanel() {
        mrfModel = new DictionaryModel(new Dictionary(mrf));
        builder.addComponent(ORIGIN_LABEL, mrfModel.bindPoint(ORIGIN_KEY));
        builder.addComponent(AXIS_LABEL, mrfModel.bindPoint(AXIS_KEY));
        builder.addComponent(OMEGA_RAD_S_LABEL, mrfModel.bindConstantDouble(OMEGA_KEY));
        mrfModel.refresh();

    }

    @Override
    public JComponent getPanel() {
        JPanel panel = builder.getPanel();
        panel.setBorder(BorderFactory.createTitledBorder(MRF_LABEL));
        panel.setName(MRF_LABEL);
        return panel;
    }

    @Override
    public void stateChanged() {
    }

    @Override
    public void loadFromDictionary(Dictionary cellZoneDictionary) {
        Dictionary d = new Dictionary(CellZoneType.MRF_KEY);
        d.merge(cellZoneDictionary);
        mrfModel.setDictionary(cellZoneDictionary);

    }

    @Override
    public Dictionary saveToDictionary() {
        return mrfModel.getDictionary();
    }

}
