package model;

import java.awt.event.KeyEvent;

public class Captura {
	private KeyEvent tecla;
	private int numeroAmostras;
	private int amostras;
	
	public Captura(int numeroAmostras) {
		this.amostras = 1;
		this.numeroAmostras = numeroAmostras;
		this.tecla = null;
	}
	
	public KeyEvent getTecla() {
		return tecla;
	}
	public void setTecla(KeyEvent tecla) {
		this.tecla = tecla;
	}
	public int getNumeroAmostras() {
		return numeroAmostras;
	}
	public void setNumeroAmostras(int numeroAmostras) {
		this.numeroAmostras = numeroAmostras;
	}
	public int getAmostras() {
		return amostras;
	}
	public void setAmostras(int amostras) {
		this.amostras = amostras;
	}
	
}
