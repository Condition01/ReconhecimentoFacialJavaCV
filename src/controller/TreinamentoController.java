package controller;

import static org.bytedeco.opencv.global.opencv_core.CV_32SC1;
import static org.bytedeco.opencv.global.opencv_imgcodecs.IMREAD_GRAYSCALE;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.io.File;
import java.nio.IntBuffer;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;

import model.Treinamento;

public class TreinamentoController {
	
	private MatVector vetorFotos;
	private Mat rotulosFotos;
	private IntBuffer rotulosBuffer;
	private File[] arquivos;

	
	public void treinar() {
		Treinamento treinamento = new Treinamento(new File("src\\fotos"));
		
		this.arquivos = treinamento.getDiretorio().listFiles(treinamento.getFiltroImagem());
		this.vetorFotos = new MatVector(arquivos.length); 
		this.rotulosFotos = new Mat(arquivos.length,1, CV_32SC1);
		this.rotulosBuffer = rotulosFotos.createBuffer(); 
		
		int contador = 0;
		
		for(File imagem : this.arquivos) {
			preencheVetorImagens(contador, imagem); 
			contador++;
		}	
		
		realizaTreinamento();
	}


	private void realizaTreinamento() {
		System.out.println("treinando...");
        FaceRecognizer eigenfaces = EigenFaceRecognizer.create(10, 0); 
		FaceRecognizer fisherfaces = FisherFaceRecognizer.create(); 
        FaceRecognizer lbph = LBPHFaceRecognizer.create(2,9,9,9,1); 
        
        eigenfaces.train(this.vetorFotos, this.rotulosFotos); 
        eigenfaces.save("src\\recursos\\classificadorEigenFaces.yml"); 
        
        fisherfaces.train(this.vetorFotos, this.rotulosFotos);
        fisherfaces.save("src\\recursos\\classificadorFisherFaces.yml");
	
        lbph.train(this.vetorFotos, this.rotulosFotos);
        lbph.save("src\\recursos\\classificadorLBPH.yml");
        System.out.println("pronto!!");
	}


	private void preencheVetorImagens(int contador, File imagem) {
		Mat foto = imread(imagem.getAbsolutePath(), IMREAD_GRAYSCALE); 
		int classe = Integer.parseInt(imagem.getName().split("\\.")[1]); 
		resize(foto, foto, new Size(160,160)); 
		
		this.vetorFotos.put(contador,foto); 
		this.rotulosBuffer.put(contador, classe);
	}
}
