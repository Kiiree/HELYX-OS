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

package eu.engys.core.project.state;

import eu.engys.core.dictionary.Dictionary;
import eu.engys.core.project.Model;

public class BuoyancyBuilder {

    public static void save(Model model, double[] gValue) {
        Dictionary g = model.getProject().getConstantFolder().getG();
        if (g != null) {
            if (model.getState().getMultiphaseModel().isOn()) {
                g.add("value", "(" + gValue[0] + " " + gValue[1] + " " + gValue[2] + ")");
                g.add("dimensions", "[0 1 -2 0 0 0 0]");
            } else if (model.getState().isEnergy()) {
                if (model.getState().isBuoyant()) {
                    g.add("value", "(" + gValue[0] + " " + gValue[1] + " " + gValue[2] + ")");
                } else {
                    g.add("value", "(0 0 0)");
                }
                g.add("dimensions", "[0 1 -2 0 0 0 0]");
            } else {
                g.remove("value");
                g.remove("dimensions");
            }
        }
        // System.out.println("BuoyancyBuilder.save() "+g);
    }

}