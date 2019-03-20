package hr.fer.zemris.java.crypto;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class UtilTest {

	@Test
	public void testByteToHex() {
		assertEquals(Util.byteToHex(new byte[] { 1, -82, 34 }), "01ae22");
	}

	@Test
	public void byteToHexNegativeOne() {
		assertEquals(Util.byteToHex(new byte[] { -1 }), "ff");
	}

	@Test
	public void byteToHex2() {
		assertEquals(Util.byteToHex(new byte[] { -1, 0, -1 }), "ff00ff");
	}

	@Test
	public void testHexToByte() {
		assertArrayEquals(Util.hexToByte("01ae22"), new byte[] { 1, -82, 34 });
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidStrinHexToByte() {
		Util.hexToByte("0123ASDasdćasd");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void oddSizeHexToByte() {
		Util.hexToByte("123");
	}
	
	@Test
	public void emptyHexToByte() {
		assertEquals(Util.hexToByte("").length, 0);
	}
	
	@Test
	public void emptyByteToHex() {
		assertEquals(Util.byteToHex(new byte[0]), "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullHexToByte() {
		Util.hexToByte(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullByteToHex() {
		Util.byteToHex(null);
	}
	
	@Test
	public void testAll() {
		byte[] b1 = Util.hexToByte("e52217e3ee213ef1ffdee3a192e2ac7e");
		assertEquals(Util.byteToHex(b1), "e52217e3ee213ef1ffdee3a192e2ac7e");		
	}
}
