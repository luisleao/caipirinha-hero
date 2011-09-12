package br.com.luisleao.caipirinhahero;

public class Nota {
	private boolean notaC = false; //1
	private boolean notaD = false; //2
	private boolean notaE = false; //4
	private boolean notaF = false; //8
	private boolean notaG = false; //16
	private boolean notaA = false; //32
	private boolean notaB = false; //64
	private boolean notaC1 = false; //128
	//private int note = 0;
	
	public void setNotaC(boolean nota) { notaC = nota; }
	public void setNotaD(boolean nota) { notaD = nota; }
	public void setNotaE(boolean nota) { notaE = nota; }
	public void setNotaF(boolean nota) { notaF = nota; }
	public void setNotaG(boolean nota) { notaG = nota; }
	public void setNotaA(boolean nota) { notaA = nota; }
	public void setNotaB(boolean nota) { notaB = nota; }
	public void setNotaC1(boolean nota) { notaC1 = nota; }

	public boolean getNotaC() { return notaC; }
	public boolean getNotaD() { return notaD; }
	public boolean getNotaE() { return notaE; }
	public boolean getNotaF() { return notaF; }
	public boolean getNotaG() { return notaG; }
	public boolean getNotaA() { return notaA; }
	public boolean getNotaB() { return notaB; }
	public boolean getNotaC1() { return notaC1; }
	
	
	public Nota(int note) { 
		setNotes(note);
		
	}
	public Nota(boolean nota_C, boolean nota_D, boolean nota_E,
			boolean nota_F, boolean nota_G, boolean nota_A,
			boolean nota_B, boolean nota_C1) {
		notaC = nota_C;
		notaD = nota_D;
		notaE = nota_E;
		notaF = nota_F;
		notaG = nota_G;
		notaA = nota_A;
		notaB = nota_B;
		notaC1 = nota_C1;
	}
	
	public int getNotes() {
		int result = 0;
		
		result += notaC ? 128 : 0;
		result += notaD ? 64 : 0;
		result += notaE ? 32 : 0;
		result += notaF ? 16 : 0;
		result += notaG ? 8 : 0;
		result += notaA ? 4 : 0;
		result += notaB ? 2 : 0;
		result += notaC1 ? 1 : 0;
		
		return result;
	}
	public void setNotes(int notes) {
		
		notaC = notes/128 >= 1;
		notes = notes % 128;
		
		notaD = notes/64 >= 1;
		notes = notes % 64;

		notaE = notes/32 >= 1;
		notes = notes % 32;

		notaF = notes/16 >= 1;
		notes = notes % 16;

		notaG = notes/8 >= 1;
		notes = notes % 8;

		notaA = notes/4 >= 1;
		notes = notes % 4;

		notaB = notes/2 >= 1;
		notes = notes % 2;

		notaC1 = notes != 0;

		//note = notes;
	}
}
