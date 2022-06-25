public class AdventOfCode25 {
	
	public static void main(String[] args) {
		part1();
	}
	
	public static void part1() {
		long devicePublic = 8421034;
		long doorPublic = 15993936;
		long subject = 7;
		long value = 1;
		long deviceSecretLoopSize = 0;
		long doorSecretLoopSize = 0;
		long encryptionKey = 0;
		while (value != devicePublic) {
			value = value * subject;
			value = value % 20201227;
			deviceSecretLoopSize++;
		}
		value = 1;
		while (value != doorPublic) {
			value = value * subject;
			value = value % 20201227;
			doorSecretLoopSize++;
		}
		value = 1;
		subject = devicePublic;
		for (int i = 0; i < doorSecretLoopSize; i++) {
			value = value * subject;
			value = value % 20201227;
		}
		long encryptionKey1 = value;
		value = 1;
		subject = doorPublic;
		for (int i = 0; i < deviceSecretLoopSize; i++) {
			value = value * subject;
			value = value % 20201227;
		}
		long encryptionKey2 = value;
		System.out.println(encryptionKey2);
	}
	
}
