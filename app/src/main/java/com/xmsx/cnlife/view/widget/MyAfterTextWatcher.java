package com.xmsx.cnlife.view.widget;

import android.text.TextWatcher;


/**
 *  没实现afterTextChanged，减少页面重复写beforeTextChanged，onTextChanged
 */

public abstract class MyAfterTextWatcher implements TextWatcher{

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


}
