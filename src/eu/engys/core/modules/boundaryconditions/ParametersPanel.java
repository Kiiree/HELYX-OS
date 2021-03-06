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


package eu.engys.core.modules.boundaryconditions;

import javax.swing.JPanel;

import eu.engys.core.dictionary.model.DictionaryModel;
import eu.engys.core.project.Model;
import eu.engys.core.project.zero.patches.BoundaryConditions;

public interface ParametersPanel {

    public String getTitle();
    public JPanel getComponent();
    public void tabChanged(Model model);
    public void stateChanged(Model model);
    public void materialsChanged(Model model);
    public DictionaryModel getDictionaryModel();
    
    public void loadFromBoundaryConditions(String patchName, BoundaryConditions bc);
	public void saveToBoundaryConditions(String patchName, BoundaryConditions bc);
	
	public void setMultipleEditing(boolean multipleSelection);
	public boolean canEdit();
	public boolean isEnabled(Model model);
	public void resetToDefault(Model model);
}
