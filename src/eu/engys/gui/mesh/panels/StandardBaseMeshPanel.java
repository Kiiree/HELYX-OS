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

package eu.engys.gui.mesh.panels;

import static eu.engys.core.project.system.BlockMeshDict.SPACING_KEY;
import static eu.engys.core.project.system.SnappyHexMeshDict.AUTO_BLOCK_MESH_KEY;

import com.google.inject.Inject;

import eu.engys.core.controller.Controller;
import eu.engys.core.project.Model;
import eu.engys.core.project.system.BlockMeshDict;
import eu.engys.core.project.system.SnappyHexMeshDict;

public class StandardBaseMeshPanel extends AbstractBaseMeshPanel {

    @Inject
    public StandardBaseMeshPanel(Model model, Controller controller) {
        super(model, controller);
    }

    @Override
    protected void loadSpacing() {
        BlockMeshDict blockMeshDict = model.getProject().getSystemFolder().getBlockMeshDict();
        if (blockMeshDict != null && blockMeshDict.found(SPACING_KEY)) {
            setBaseMeshSpacing(blockMeshDict.lookupDouble(SPACING_KEY));
        }
    }

    @Override
    public void save() {
        super.save();
        fixSpacing();
        fixAutoBlockMesh();
    }

    private void fixAutoBlockMesh() {
        SnappyHexMeshDict snappyDict = model.getProject().getSystemFolder().getSnappyHexMeshDict();
        if (snappyDict != null) {
            if (model.getGeometry().isAutoBoundingBox()) {
                snappyDict.add(AUTO_BLOCK_MESH_KEY, String.valueOf(true));
            } else {
                snappyDict.add(AUTO_BLOCK_MESH_KEY, String.valueOf(false));
            }
        }
    }

    private void fixSpacing() {
        BlockMeshDict blockMeshDict = model.getProject().getSystemFolder().getBlockMeshDict();
        if (blockMeshDict != null) {
            if (isUserDefined() || isFromFile()) {
                blockMeshDict.remove(SPACING_KEY);
            } else if (isAutomatic()) {
                blockMeshDict.add(SPACING_KEY, Double.toString(getBaseMeshSpacing()));
            }
        }
    }
}
