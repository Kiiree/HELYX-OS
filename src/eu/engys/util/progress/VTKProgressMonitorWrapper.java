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

package eu.engys.util.progress;

import vtk.vtkAlgorithm;

public class VTKProgressMonitorWrapper {

    private vtkAlgorithm algo;
    private ProgressMonitor monitor;
    private String title;

    public VTKProgressMonitorWrapper(String title, vtkAlgorithm algo, ProgressMonitor monitor) {
        this.title = title;
        this.algo = algo;
        this.monitor = monitor;
    }

    public void onProgress() {
        monitor.setCurrent(null, (int) (algo.GetProgress() * 100));
    }

    public void onStart() {
        monitor.infoN("Loading " + title + "... ", 1);
    }

    public void onEnd() {
        monitor.info("done");
    }

}
