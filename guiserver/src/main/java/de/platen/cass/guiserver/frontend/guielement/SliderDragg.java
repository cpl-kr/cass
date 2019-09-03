package de.platen.cass.guiserver.frontend.guielement;

public class SliderDragg {

	private final int rangePlus;
	private final int rangeMinus;
	private final int stepSize;
	private final int moveCount;
	
	public SliderDragg(int rangePlus, int rangeMinus, int stepSize, int moveCount) {
		this.rangePlus = rangePlus;
		this.rangeMinus = rangeMinus;
		this.stepSize = stepSize;
		this.moveCount = moveCount;
	}

	public int getRangePlus() {
		return rangePlus;
	}

	public int getRangeMinus() {
		return rangeMinus;
	}

	public int getStepSize() {
		return stepSize;
	}

	public int getMoveCount() {
		return moveCount;
	}
}
