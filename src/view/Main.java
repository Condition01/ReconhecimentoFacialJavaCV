package view;

import org.bytedeco.javacv.FrameGrabber.Exception;

import controller.CapturaController;
import controller.Reconhecimento;
import controller.TreinamentoController;

public class Main {

	public static void main(String[] args) {
//		CapturaController cp = new CapturaController();
//
//		try {
//			cp.capturar();
//		} catch (Exception | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		TreinamentoController tc = new TreinamentoController();
//		tc.treinar();
		Reconhecimento rc = new Reconhecimento();
		try {
			System.out.println("Reconhecer!");
			rc.reconhecer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
