package org.osgeo.grass.i;

import org.jgrasstools.grass.utils.ModuleSupporter;

import oms3.annotations.Author;
import oms3.annotations.Documentation;
import oms3.annotations.Label;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.UI;
import oms3.annotations.Keywords;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;
import oms3.annotations.Status;

@Description("Inverse Fast Fourier Transform (IFFT) for image processing.")
@Author(name = "Grass Developers Community", contact = "http://grass.osgeo.org")
@Keywords("imagery, FFT")
@Label("Grass/Imagery Modules")
@Name("i__ifft")
@Status(Status.CERTIFIED)
@License("General Public License Version >=2)")
public class i__ifft {

	@UI("infile,grassfile")
	@Description("Name of input raster map (image fft, real part)")
	@In
	public String $$real_imagePARAMETER;

	@UI("infile,grassfile")
	@Description("Name of input raster map (image fft, imaginary part")
	@In
	public String $$imaginary_imagePARAMETER;

	@UI("outfile,grassfile")
	@Description("Name for output raster map")
	@In
	public String $$output_imagePARAMETER;

	@Description("Allow output files to overwrite existing files")
	@In
	public boolean $$overwriteFLAG = false;

	@Description("Verbose module output")
	@In
	public boolean $$verboseFLAG = false;

	@Description("Quiet module output")
	@In
	public boolean $$quietFLAG = false;


	@Execute
	public void process() throws Exception {
		ModuleSupporter.processModule(this);
	}

}
