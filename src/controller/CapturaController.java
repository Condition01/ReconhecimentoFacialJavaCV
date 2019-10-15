package controller;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

import java.awt.event.KeyEvent;
import java.util.Scanner;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;

import model.Captura;

public class CapturaController {

	private CascadeClassifier detectorFace;
	private OpenCVFrameConverter.ToMat converteMat;
	private OpenCVFrameGrabber camera;
	private CanvasFrame cFrame;

	public CapturaController() {
		this.detectorFace = new CascadeClassifier("src\\recursos\\haarcascade-frontalface-alt.xml");
		this.converteMat = new OpenCVFrameConverter.ToMat();
		this.camera = new OpenCVFrameGrabber(0);
		this.cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / this.camera.getGamma());
	}

	public void capturar() throws FrameGrabber.Exception, InterruptedException {
		Captura cap = new Captura(25);
		Frame frameCapturado = null;
		Mat imgColorida = new Mat();
		int idPessoa = pegaId();
		this.camera.start();

		while ((frameCapturado = this.camera.grab()) != null) {
			imgColorida = this.converteMat.convert(frameCapturado);
			Mat imgCinza = new Mat();
			cvtColor(imgColorida, imgCinza, COLOR_BGRA2GRAY);
			RectVector facesDetectadas = new RectVector();
			this.detectorFace.detectMultiScale(imgCinza, facesDetectadas, 1.1, 1, 0, new Size(150, 150), new Size(500, 500));
			cap.setTecla(adicionaDelayKeyevent(cap.getTecla(), this.cFrame, 5));
			
			for (int i = 0; i < facesDetectadas.size(); i++) {
				Rect dadosFace = facesDetectadas.get(0); 
				rectangle(imgColorida, dadosFace, new Scalar(0, 0, 255, 0));
				Mat faceCapturada = new Mat(imgCinza, dadosFace);
				resize(faceCapturada, faceCapturada, new Size(160, 160));
				cap.setTecla(adicionaDelayKeyevent(cap.getTecla(), this.cFrame, 5));
				salvaImagem(cap, idPessoa, faceCapturada); //salva as imagens capturadas
			}
			cap.setTecla(adicionaDelayKeyevent(cap.getTecla(), this.cFrame, 20));
			
			if (this.cFrame.isVisible()) {
				this.cFrame.showImage(frameCapturado);
			}
			
			if (cap.getAmostras() > cap.getNumeroAmostras()) {
				break;
			}
		}
		this.cFrame.dispose();
		this.camera.stop();
	}

	private void salvaImagem(Captura cap, int idPessoa, Mat faceCapturada) {
		if (cap.getTecla() != null) {
			if (cap.getTecla().getKeyChar() == 'q') {
				if (cap.getAmostras() <= cap.getNumeroAmostras()) {
					imwrite("src\\fotos\\pessoa." + idPessoa + "." + cap.getAmostras() + ".jpg", faceCapturada);
					System.out.println("Foto " + cap.getAmostras() + " capturada\n");
					cap.setAmostras(cap.getAmostras() + 1);
				}
			}
			cap.setTecla(null);
		}
	}

	private int pegaId() {
		System.out.println("Digite seu id: ");
		Scanner cadastro = new Scanner(System.in);
		int idPessoa = cadastro.nextInt();
		return idPessoa;
	}

	private KeyEvent adicionaDelayKeyevent(KeyEvent tecla, CanvasFrame cFrame, int valor)
			throws InterruptedException {
		if (tecla == null) {
			tecla = cFrame.waitKey(valor);
		}
		return tecla;
	}

}
