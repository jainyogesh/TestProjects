import java.io.*;

public class CandidateCode4 {
	public static String criticalBridges(int input1, String input2) {
		StringBuilder result = new StringBuilder();

		String[] testCases = input2.split("\\}\\),");

		if (testCases.length > 1) {
			result.append('{');
		}

		for (int i = 0; i < testCases.length; i++) {
			if (i > 0) {
				result.append(',');
			}
			String testCase = testCases[i];
			String[] testCaseData = testCase.split("\\},\\{");
			String[] icelandNames = testCaseData[0].substring(2).split(",");
			Iceland[] icelands = new Iceland[icelandNames.length];
			for (int ic = 0; ic < icelands.length; ic++) {
				icelands[ic] = new Iceland(icelandNames[ic]);
			}
			String[] bridges = i == testCases.length - 1 ? testCaseData[1]
					.substring(0, testCaseData[1].length() - 2).split("\\),")
					: testCaseData[1].split("\\),");
			StringBuilder allBridges = new StringBuilder();
			for (int j = 0; j < bridges.length; j++) {
				String bridge = j == bridges.length - 1 ? bridges[j].substring(
						1, bridges[j].length() - 1) : bridges[j].substring(1);
				String connectedIcelands[] = bridge.split(",");
				if (allBridges.length() > 0) {
					allBridges.append(',');
				}
				allBridges.append('(').append(bridge).append(')');
				if (connectedIcelands.length == 2) {
					Iceland ic1 = null;
					Iceland ic2 = null;
					int match = 0;
					for (int ic = 0; ic < icelands.length; ic++) {
						if (connectedIcelands[0].equals(icelands[ic].getName())) {
							ic1 = icelands[ic];
							match++;
						} else if (connectedIcelands[1].equals(icelands[ic]
								.getName())) {
							ic2 = icelands[ic];
							match++;
						}
						if (match == 2)
							break;
					}
					if (ic1 != null && ic2 != null) {
						Bridge b = new Bridge(ic1, ic2);
						ic1.addBridge(b);
						ic2.addBridge(b);

					}
				}

			}

			int criticalBridgeCount = 0;
			for (int ic = 0; ic < icelands.length; ic++) {
				Iceland iceland = icelands[ic];
				if (iceland.getBrigeCount() == 0) {
					criticalBridgeCount = bridges.length;
					result.append('{').append(allBridges);
					break;
				}

				List<Bridge> bridgeList = iceland.getBridges();
				for (int bl = 0; bl < bridgeList.size(); bl++) {
					Bridge br = bridgeList.get(bl);
					if (!br.isCritical()) {
						br.breakBridge();
						boolean isCritical = checkCritical(icelands);
						if (isCritical) {
							br.setCritical();
							if (criticalBridgeCount == 0) {
								result.append('{');
							} else {
								result.append(',');
							}
							criticalBridgeCount++;
							result.append(br);
						}
						br.reconstruct();
					}
				}
			}

			if (criticalBridgeCount == 0) {
				result.append("{NA");
			}
			result.append('}');

		}

		if (testCases.length > 1) {
			result.append('}');
		}
		return result.toString();
	}

	private static boolean checkCritical(Iceland[] icelands) {
		Counter sum = new Counter();
		visit(icelands[0], sum);
		for (int i = 0; i < icelands.length; i++) {
			Iceland iceland = icelands[i];
			iceland.reset();
		}

		return sum.value() != icelands.length;
	}

	private static void visit(Iceland iceland, Counter sum) {
		if (iceland.isVisited())
			return;
		iceland.setVisited();
		sum.increment();
		List<Bridge> bridgeList = iceland.getBridges();
		for (int j = 0; j < bridgeList.size(); j++) {
			Bridge bridge = bridgeList.get(j);
			if (!bridge.isBroken()) {
				Iceland ic2 = bridge.getOtherConnection(iceland);
				visit(ic2, sum);
			}
		}
	}

	private static class Iceland {

		String icelandName;
		List<Bridge> bridgeList = new List<Bridge>();
		boolean visited;

		private Iceland(String icelandName) {
			this.icelandName = icelandName;
		}

		private void addBridge(Bridge bridge) {
			bridgeList.add(bridge);
		}

		private void setVisited() {
			this.visited = true;
		}

		private boolean isVisited() {
			return this.visited;
		}

		private void reset() {
			this.visited = false;
		}

		private String getName() {
			return this.icelandName;
		}

		private int getBrigeCount() {
			return bridgeList.size();
		}

		private List<Bridge> getBridges() {
			return bridgeList;
		}

		private String getBridgesAsString() {
			return bridgeList.toString();
		}
	}

	private static class Bridge {
		Iceland iceland1;
		Iceland iceland2;
		boolean broken;
		boolean critical;

		private Bridge(Iceland iceland1, Iceland iceland2) {
			if (iceland1.getName().compareTo(iceland2.getName()) < 0) {
				this.iceland1 = iceland1;
				this.iceland2 = iceland2;
			}else{
				this.iceland1 = iceland2;
				this.iceland2 = iceland1;
			}
		}

		private void breakBridge() {
			this.broken = true;
		}

		private void reconstruct() {
			this.broken = false;
		}

		private boolean isBroken() {
			return this.broken;
		}

		private void setCritical() {
			this.critical = true;
		}

		private boolean isCritical() {
			return this.critical;
		}

		private boolean connects(String iceland) {
			return iceland.equals(iceland1) || iceland.equals(iceland2);
		}

		private Iceland getOtherConnection(Iceland ic) {
			if (iceland1.getName().equals(ic.getName()))
				return iceland2;

			if (iceland2.getName().equals(ic.getName()))
				return iceland1;

			return null;
		}

		public String toString() {
			return new StringBuilder().append('(').append(iceland1.getName())
					.append(',').append(iceland2.getName()).append(')')
					.toString();
		}

	}

	private static class List<E> {
		Object[] buckets = null;
		int count = 0;

		private List() {
			buckets = new Object[100];
		}

		private List(E[] arr) {
			buckets = arr;
			count = arr.length;
		}

		private void add(E obj) {
			if (count + 1 > buckets.length) {
				Object[] copy = new Object[buckets.length * 2];
				System.arraycopy(buckets, 0, copy, 0, buckets.length);
				buckets = copy;
			}
			buckets[count] = obj;
			count++;
		}

		private E get(int index) {
			return (E) buckets[index];
		}

		private int size() {
			return count;
		}

		public String toString() {
			StringBuilder res = new StringBuilder();
			// res.append('{');
			for (int i = 0; i < count - 1; i++) {
				res.append(buckets[i]).append(',');
			}
			res.append(buckets[count - 1]);
			// res.append('}');
			return res.toString();
		}
	}

	private static class Counter {
		int counter;

		private Counter() {
			counter = 0;
		}

		private void add(int value) {
			counter = counter + value;
		}

		private void increment() {
			counter++;
		}

		private int value() {
			return counter;
		}
	}
}