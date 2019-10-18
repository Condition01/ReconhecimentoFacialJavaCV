package controller;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.opencv.global.opencv_imgproc.FONT_HERSHEY_PLAIN;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;

import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.putText;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class Reconhecimento {
	
	List<String> pessoas;
	
	public Reconhecimento() {
		pessoas = new ArrayList<String>();
		this.pessoas.add("");
		this.pessoas.add("Bete");
		this.pessoas.add("Bruno");
		this.pessoas.add("Matheus");
	}
	
	public void reconhecer() throws Exception {
		OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
		OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
		camera.start();

		CascadeClassifier detectorFace = new CascadeClassifier("src\\recursos\\haarcascade-frontalface-alt.xml");

		FaceRecognizer reconhecedor = null;
		reconhecedor = escolherReconhecedor(reconhecedor);

		CanvasFrame cFrame = new CanvasFrame("Reconhecimento", CanvasFrame.getDefaultGamma() / camera.getGamma());
		Frame frameCapturado = null;
		Mat imagemColorida = new Mat();

		while ((frameCapturado = camera.grab()) != null) {

			imagemColorida = converteMat.convert(frameCapturado);
			Mat imagemCinza = new Mat();
			cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
			RectVector facesDetectadas = new RectVector();
			detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));
			for (int i = 0; i < facesDetectadas.size(); i++) {

				Rect dadosFace = facesDetectadas.get(0);
				rectangle(imagemColorida, dadosFace, new Scalar(0, 0, 255, 0));
				Mat faceCapturada = new Mat(imagemCinza, dadosFace);
				resize(faceCapturada, faceCapturada, new Size(160, 160));

				IntPointer rotulo = new IntPointer(1);
				DoublePointer confianca = new DoublePointer(1); //
				reconhecedor.predict(faceCapturada, rotulo, confianca);

				int predicao = rotulo.get(0);
				String nome;
				System.out.println(predicao);
				
				if (predicao == -1) {
					nome = "desconhecido";
				} else {
					nome = this.pessoas.get(predicao) + " - " + confianca.get(0);
				}

				int x = Math.max(dadosFace.tl().x() - 10, 0);
				int y = Math.max(dadosFace.tl().y() - 10, 0);

				putText(imagemColorida, nome, new Point(x, y), FONT_HERSHEY_PLAIN, 1.4, new Scalar(0, 255, 0, 0));
			}

			if (cFrame.isVisible()) {
				cFrame.showImage(frameCapturado);
			}

		}
		cFrame.dispose();
		camera.stop();
	}

	public FaceRecognizer escolherReconhecedor(FaceRecognizer reconhecedor) {
		System.out.println(
				"Digite 1 para utilizar o EigenFaces+\nDigite 2 para utilizar o FisherFaces+\nDigite outro valor para utilizar o LBPH");
		Scanner sc = new Scanner(System.in);
		int opcao = sc.nextInt();
		sc.nextLine();

		if (opcao == 1) {
			reconhecedor = EigenFaceRecognizer.create(); // *antes: createEigenFaceRecognizer();
			reconhecedor.read("src\\recursos\\classificadorEigenFaces.yml"); // aqui ele vai aprender com o nosso
																				// arquivo YML
			reconhecedor.setThreshold(4000);
		} else if (opcao == 2) {
			reconhecedor = FisherFaceRecognizer.create();
			reconhecedor.read("src\\recursos\\classificadorFisherFaces.yml");
			reconhecedor.setThreshold(1000);
		} else {
			reconhecedor = LBPHFaceRecognizer.create();
			reconhecedor.read("src\\recursos\\classificadorLBPH.yml");
		}
		return reconhecedor;
	}

}
