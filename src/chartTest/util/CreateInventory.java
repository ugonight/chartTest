package chartTest.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import chartTest.entity.Inventory;

public class CreateInventory {
	static int prodId = 0;

	public static List<Inventory> getInventoriyLog() {
		LocalDateTime now = LocalDateTime.now();
		int ny = now.getYear();
		int y, m, d;
		List<Inventory> list = new ArrayList<Inventory>();

		for (int i = 0; i < 1000; i++) {
			Inventory inventory = new Inventory();
			inventory.setAmount((int) (Math.random() * 100));
			if (i > 0)
				inventory.setAmountD(list.get(i - 1).getAmount() - inventory.getAmount());
			y = ny - (int) (10 * Math.random());
			m = (int) (Math.random() * 12 + 1);
			d = (int) (28 * Math.random() + 1);
			inventory.setDate(LocalDateTime.of(y, m, d, 0, 0));
			inventory.setProdName("テスト" + prodId);
			inventory.setProdId(prodId);
			list.add(inventory);
		}
		prodId++;
		return list;
	}
}
