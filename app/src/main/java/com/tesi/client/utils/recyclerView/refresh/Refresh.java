package com.tesi.client.utils.recyclerView.refresh;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tesi.entity.Prodotto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Refresh {

	public static void run(@NonNull FragmentActivity activity, List<Prodotto> prodotti, RecyclerView.Adapter adapter, SwipeRefreshLayout refreshLayout, UpdateMethod method) {
		new Thread(()->{
			method.run();
			List<Prodotto> newProdotti=method.getList();

			if (newProdotti !=null && !newProdotti.isEmpty()) {
				activity.runOnUiThread(()->{
					DiffUtil.DiffResult diffResult=DiffUtil.calculateDiff(new ProdottoDiffCallback(prodotti, newProdotti));
					Set<Prodotto> noDuplicates=new HashSet<>(newProdotti);
					noDuplicates.addAll(prodotti);

					prodotti.clear();
					prodotti.addAll(noDuplicates);

					diffResult.dispatchUpdatesTo(adapter);
				});
			}
			refreshLayout.setRefreshing(false);
		}).start();
	}
}
