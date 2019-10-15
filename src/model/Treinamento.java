package model;

import java.io.File;
import java.io.FilenameFilter;

public class Treinamento {
	private File diretorio;
	private FilenameFilter filtroImagem;
	
	public Treinamento(File dir) {
		this.diretorio = dir;
		this.filtroImagem = new FilenameFilter() { //é um filtro pro nome dos arquivos
			@Override
			public boolean accept(File dir, String name) { //vai retornar só arquivos de imagens
				return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
			}
		};
	}

	public File getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(File diretorio) {
		this.diretorio = diretorio;
	}

	public FilenameFilter getFiltroImagem() {
		return filtroImagem;
	}

	public void setFiltroImagem(FilenameFilter filtroImagem) {
		this.filtroImagem = filtroImagem;
	}
	
}
