package com.autowheel.bangbang.ui.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.autowheel.bangbang.base.BaseViewBindingAdapter;
import com.autowheel.bangbang.databinding.ItemMainBinding;

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
