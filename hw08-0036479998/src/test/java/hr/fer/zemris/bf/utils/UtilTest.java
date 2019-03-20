package hr.fer.zemris.bf.utils;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class UtilTest {

	@Test
	public void testIndexToByteArray1() {
		assertArrayEquals(new byte[] { 1, 1 }, Util.indexToByteArray(3, 2));
	}

	@Test
	public void testIndexToByteArray2() {
		assertArrayEquals(new byte[] { 0, 0, 1, 1 },
				Util.indexToByteArray(3, 4));
	}

	@Test
	public void testIndexToByteArray3() {
		assertArrayEquals(new byte[] { 0, 0, 0, 0, 1, 1 },
				Util.indexToByteArray(3, 6));
	}

	@Test
	public void testIndexToByteArrayInfoLoss() {
		assertArrayEquals(new byte[] { 0, 0, 1, 1 },
				Util.indexToByteArray(19, 4));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexToByteArrayNegativeSize() {
		Util.indexToByteArray(3, -3);
	}

	@Test
	public void testIndexToByteArrayNegative1() {
		assertArrayEquals(
				new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
				Util.indexToByteArray(-2, 32));
	}

	@Test
	public void testIndexToByteArrayNegative2() {
		assertArrayEquals(
				new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
						1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				Util.indexToByteArray(-1, 32));
	}

	@Test
	public void testIndexToByteArrayNegative3() {
		assertArrayEquals(
				new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0 },
				Util.indexToByteArray(-2, 16));
	}

	@Test
	public void testIndexToByteArrayZero() {
		assertArrayEquals(new byte[] { 0, 0, 0 }, Util.indexToByteArray(0, 3));
	}
}
