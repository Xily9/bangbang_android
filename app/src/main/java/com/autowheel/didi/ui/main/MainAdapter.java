package com.autowheel.didi.ui.main;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.autowheel.didi.base.BaseAdapter;
import com.autowheel.didi.base.BaseViewBindingAdapter;
import com.autowheel.didi.databinding.ItemMainBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Xily on 2020/3/5.
 */
public class MainAdapter extends BaseViewBindingAdapter<ItemMainBinding, String> {

    public MainAdapter(@Nullable List<? extends String> list) {
        super(list);
    }

    @NotNull
    @Override
    public ItemMainBinding initViewBinding(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup parent) {
        return ItemMainBinding.inflate(layoutInflater, parent, false);
    }

    @Override
    protected void onBindViewHolder(@NotNull ItemMainBinding viewBinding, int position, String value) {
        viewBinding.text.setText(value);
    }
}
