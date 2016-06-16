package com.mygdx.utils;

public class InfoTable {
	int numlight;
	int numheavy;
	private float[][] FAD;
	private float[][] FTA;
	private float[][] FTD;
	
	public InfoTable(int light, int heavy, float[][] FAD,float[][] FTA,float[][] FTD){
		this.numlight = light;
		this.numheavy = heavy;
		this.FAD=FAD;
		this.FTA=FTA;
		this.FTD=FTD;
	}

	public int getNumlight() {
		return numlight;
	}

	public void setNumlight(int numlight) {
		this.numlight = numlight;
	}

	public int getNumheavy() {
		return numheavy;
	}

	public void setNumheavy(int numheavy) {
		this.numheavy = numheavy;
	}

	public float[][] getFAD() {
		return FAD;
	}

	public void setFAD(float[][] fAD) {
		FAD = fAD;
	}

	public float[][] getFTA() {
		return FTA;
	}

	public void setFTA(float[][] fTA) {
		FTA = fTA;
	}

	public float[][] getFTD() {
		return FTD;
	}

	public void setFTD(float[][] fTD) {
		FTD = fTD;
	}
	
}
