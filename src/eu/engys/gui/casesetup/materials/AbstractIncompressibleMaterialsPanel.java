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

package eu.engys.gui.casesetup.materials;

import static eu.engys.core.project.constant.TransportProperties.CP_KEY;
import static eu.engys.core.project.constant.TransportProperties.LAMBDA_KEY;
import static eu.engys.core.project.constant.TransportProperties.MATERIAL_NAME_KEY;
import static eu.engys.core.project.constant.TransportProperties.MU_KEY;
import static eu.engys.core.project.constant.TransportProperties.NU_KEY;
import static eu.engys.core.project.constant.TransportProperties.PRT_KEY;
import static eu.engys.core.project.constant.TransportProperties.P_REF_KEY;
import static eu.engys.core.project.constant.TransportProperties.RHO_KEY;
import static eu.engys.core.project.constant.TransportProperties.SIGMA_KEY;
import static eu.engys.core.project.constant.TransportProperties.T_REF_KEY;
import static eu.engys.util.Symbols.CP;
import static eu.engys.util.Symbols.DENSITY;
import static eu.engys.util.Symbols.DOT;
import static eu.engys.util.Symbols.MINUS_ONE;
import static eu.engys.util.Symbols.MU_MEASURE;
import static eu.engys.util.Symbols.NU_MEASURE;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import eu.engys.core.dictionary.Dictionary;
import eu.engys.core.dictionary.DimensionedScalar;
import eu.engys.core.dictionary.model.DictionaryModel;
import eu.engys.core.modules.materials.MaterialsBuilder;
import eu.engys.core.project.Model;
import eu.engys.core.project.state.State;
import eu.engys.util.DimensionalUnits;
import eu.engys.util.ui.builder.PanelBuilder;
import eu.engys.util.ui.textfields.DoubleField;
import eu.engys.util.ui.textfields.StringField;

public abstract class AbstractIncompressibleMaterialsPanel implements IncompressibleMaterialsPanel, PropertyChangeListener {

    public static final String REFERENCE_TEMPERATURE_K_LABEL = "Reference Temperature [K]";
    public static final String REFERENCE_ABSOLUTE_PRESSURE_PA_LABEL = "Reference (absolute) Pressure [Pa]";
    public static final String THERMAL_CONDUCTIVITY_LABEL = "Thermal Conductivity [W/m" + DOT + "K]";
    public static final String TURBULENT_PRANDTL_NUMBER_LABEL = "Turbulent Prandtl Number";
    public static final String SPECIFIC_HEAT_CAPACITY_LABEL = "Specific Heat Capacity " + CP;
    public static final String KINEMATIC_VISCOSITY_LABEL = "Kinematic Viscosity " + NU_MEASURE;
    public static final String DYNAMIC_VISCOSITY_LABEL = "Dynamic Viscosity " + MU_MEASURE;
    public static final String DENSITY_LABEL = "Density " + DENSITY;
    public static final String THERMAL_EXPANTION_COEFF_LABEL = "Thermal Expansion Coefficient [K" + MINUS_ONE + "]";

    protected DictionaryModel incompressibleModel = new DictionaryModel("newMaterial", getEmptyMaterial());

    private StringField nameField;
    private DoubleField rho;
    private DoubleField mu;
    private DoubleField nu;

    protected PanelBuilder builder;
    protected MaterialsBuilder materialsBuilder;
    
    public AbstractIncompressibleMaterialsPanel() {
        this.builder = new PanelBuilder();
    }
    
    public abstract Dictionary getMaterial(Model model);

    public abstract void setMaterial(Dictionary material);
    
    @Override
    public void setEnabled(boolean enabled) {
        builder.setEnabled(enabled);
    }

    @Override
    public void stateChanged(State state) {
    }

    @Override
    public StringField getNameField() {
        return nameField;
    }

    protected void buildIncompressibleMaterialPanel() {
        builder.addComponent("Name", nameField = incompressibleModel.bindLabel(MATERIAL_NAME_KEY));
        builder.addComponent(DENSITY_LABEL, rho = incompressibleModel.bindDimensionedDouble(RHO_KEY, DimensionalUnits.KG_M3, 0D, Double.MAX_VALUE));
        builder.addComponent(DYNAMIC_VISCOSITY_LABEL, mu = incompressibleModel.bindDimensionedDouble(MU_KEY, DimensionalUnits.KG_MS, 0D, Double.MAX_VALUE));
        builder.addComponent(KINEMATIC_VISCOSITY_LABEL, nu = incompressibleModel.bindDimensionedDouble(NU_KEY, DimensionalUnits.M2_S, 0D, Double.MAX_VALUE));

        nameField.setEnabled(false);

        rho.addPropertyChangeListener("value", this);
        mu.addPropertyChangeListener("value", this);
        nu.addPropertyChangeListener("value", this);

        builder.addComponent(SPECIFIC_HEAT_CAPACITY_LABEL, incompressibleModel.bindDimensionedDouble(CP_KEY, DimensionalUnits.M2_S2K, 0D, Double.MAX_VALUE));
        builder.addComponent(TURBULENT_PRANDTL_NUMBER_LABEL, incompressibleModel.bindDimensionedDouble(PRT_KEY, DimensionalUnits.NONE, 0D, Double.MAX_VALUE));
        builder.addComponent(THERMAL_CONDUCTIVITY_LABEL, incompressibleModel.bindDimensionedDouble(LAMBDA_KEY, DimensionalUnits.KGM_S3K, 0D, Double.MAX_VALUE));
        builder.addComponent(REFERENCE_ABSOLUTE_PRESSURE_PA_LABEL, incompressibleModel.bindDimensionedDouble(P_REF_KEY, DimensionalUnits.KG_MS2, 0D, Double.MAX_VALUE));
        builder.addComponent(REFERENCE_TEMPERATURE_K_LABEL, incompressibleModel.bindDimensionedDouble(T_REF_KEY, DimensionalUnits.K, 0D, Double.MAX_VALUE));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == nu) {
            double rhoValue = rho.getDoubleValue();
            double nuValue = nu.getDoubleValue();

            mu.setDoubleValue(nuValue * rhoValue);
        } else if (evt.getSource() == rho || evt.getSource() == mu) {
            double rhoValue = rho.getDoubleValue();
            double muValue = mu.getDoubleValue();

            nu.setDoubleValue(rhoValue == 0.0 ? 0.0 : muValue / rhoValue);
        }
    }

    protected void adjustNuIfNeeded(Dictionary transportProps) {
        if (transportProps != null && !transportProps.found(NU_KEY)) {
            if (transportProps.found(RHO_KEY) && transportProps.found(MU_KEY)) {
                double rho = Double.valueOf(transportProps.lookupScalar(RHO_KEY).doubleValue());
                double mu = Double.valueOf(transportProps.lookupScalar(MU_KEY).doubleValue());
                double nuValue = mu == 0.0 ? 0.0 : mu / rho;
                transportProps.add(new DimensionedScalar(NU_KEY, Double.toString(nuValue), DimensionalUnits.M2_S));
            }
        }
    }

    @Override
    public Dictionary saveSigma(Model model, Dictionary sigmaDict) {
        Dictionary dict = new Dictionary(SIGMA_KEY);
        if (model.getState().getMultiphaseModel().isMultiphase()) {
            if (sigmaDict.found(SIGMA_KEY)) {
                dict.add(sigmaDict.lookupScalar(SIGMA_KEY));
            }
        }
        return dict;
    }

}
