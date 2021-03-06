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


package eu.engys.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FormatUtil {

	private static final DecimalFormat decFormat = new DecimalFormat("#.#", new DecimalFormatSymbols(Locale.US));
	private static final DecimalFormat centsFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
	private static final DecimalFormat millisFormat = new DecimalFormat("#.###", new DecimalFormatSymbols(Locale.US));
	
	public interface Formatter {
		String toCents();
		String toMillis();
	}
	
	private static class FormatterImpl implements Formatter {

		private double value;
		
		public FormatterImpl(double value) {
			this.value = value;
		}
		
		@Override
		public String toCents() {
			return centsFormat.format(value);
		}
		
		@Override
		public String toMillis() {
			return millisFormat.format(value);
		}
		
		@Override
		public String toString() {
			return toCents();
		}
	}
	
	private static class ArrayFormatterImpl implements Formatter {

		private double[] value;
		
		public ArrayFormatterImpl(double[] value) {
			this.value = value;
		}
		
		@Override
		public String toCents() {
			return format(centsFormat);
		}
		
		@Override
		public String toMillis() {
			return format(millisFormat);
		}
		
		private String format(DecimalFormat format){
			StringBuilder b = new StringBuilder();
			b.append('[');
			for (int i=0; i<value.length; i++) {
				b.append(format.format(value[i]));
				if (i == value.length - 1) {
					b.append(']');
				} else {
					b.append(',');
				}
			}
			return b.toString();
		}
		
		@Override
		public String toString() {
			return toCents();
		}
	}
	
	public static Formatter format(double d) {
		return new FormatterImpl(d);
	}
	
	public static Formatter format(double[] d) {
		return new ArrayFormatterImpl(d);
	}
	
	
}
