package chartTest.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import chartTest.entity.Inventory;

public class AnalyzeInventory {

	/**
	 *
	 * @param inventories
	 * @param dDay 区間日数
	 * @param divideNum 分割数
	 * @param shiftNum シフト数
	 * @return 集計後のリスト（区間ごとの合計値、区間名）
	 */
	public static List<Pair<Integer, String>> dividePeriod(List<Inventory> inventories, int dDay, int divideNum,
			int shiftNum) {
		List<Pair<Integer, String>> list = new ArrayList<>();

		// 日付で並び替える（降順）
		Collections.sort(inventories, (i1, i2) -> i2.getDate().compareTo(i1.getDate()));

		// 現在時刻を基準に遡っていく
		LocalDateTime dst = LocalDateTime.now().minusDays(dDay * (shiftNum + 1));
		int sum = 0;
		String pName = "";
		// リストのコピーを作る
		List<Inventory> invlist = new ArrayList<>(inventories);

		// シフト数分のデータを飛ばす
		LocalDateTime offset = LocalDateTime.now().minusDays(dDay * shiftNum);
		while (!invlist.isEmpty() && invlist.get(0).getDate().compareTo(offset) > 0) {
			invlist.remove(0);
		}

		for (int i = 0; i < divideNum; i++) {
			// 区間の間合計値を足す
			while (!invlist.isEmpty() && invlist.get(0).getDate().compareTo(dst) >= 0) {
				sum += invlist.get(0).getAmountD();
				invlist.remove(0);
			}
			// 区間の最後なら合計値をaddする。
			pName = dst.plusDays(dDay).format(DateTimeFormatter.ISO_DATE);
			list.add(Pair.of(sum, pName));
			sum = 0;
			dst = dst.minusDays(dDay);
		}

		// 昇順に戻す
		Collections.reverse(list);

//		for (Pair<Integer, String> data : list) {
//			System.out.println(data.getLeft() + " , " + data.getRight());
//		}

		return list;
	}
}
