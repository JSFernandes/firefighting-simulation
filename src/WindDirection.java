
public enum WindDirection {
	N	(0),
	NE	(1),
	E	(2),
	SE	(3),
	S	(4),
	SW	(5),
	W	(6),
	NW	(7);

	private int value_;

	WindDirection(int value) {
		value_ = value;
	}

	public int value() {
		return value_;
	}

	public int index(WindDirection w) {
		return (value_ - (w.value_ - 1) + 8) % 8; // quantidade de lls
	}
}
