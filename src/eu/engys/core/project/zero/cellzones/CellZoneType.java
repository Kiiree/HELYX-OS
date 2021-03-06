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

package eu.engys.core.project.zero.cellzones;

import eu.engys.core.dictionary.Dictionary;
import eu.engys.core.modules.cellzones.CellZonePanel;

public interface CellZoneType extends Comparable<CellZoneType> {

    public static final String MRF_KEY = "mrf";
    public static final String POROUS_KEY = "porous";
    public static final String THERMAL_KEY = "thermal";
    public static final String HUMIDITY_KEY = "humidity";
    public static final String SLIDING_MESH_KEY = "sliding";

    public abstract String getKey();

    public abstract String getLabel();

    public abstract Dictionary getDefaultDictionary();

    public abstract CellZonePanel getPanel();

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean enabled);

    public abstract void updateStatusByState();

};
