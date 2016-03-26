package com.hustunique.jianguo.openkeychaindemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustunique.jianguo.openkeychaindemo.R;
import com.hustunique.jianguo.openkeychaindemo.adapters.SearchAdapter;
import com.hustunique.jianguo.openkeychaindemo.utils.AnimationUtil;


/**
 * Created by JianGuo on 3/19/16.
 * Integrate ZXing into the searchView, WIP
 */
public class MaterialSearchView extends FrameLayout implements Filter.FilterListener {
    public static final int REQUEST_VOICE = 9999;
    Context mContext;
    ImageButton mBackBtn, mQrBtn, mClearBtn;
    EditText mSearchTextView;
    RelativeLayout mSearchTopBar;
    ListView mSuggestionListView;
    private View mTintView;
    private MenuItem mMenuItem;
    private boolean mIsSearchOpen = false;
    private int mAnimationDuration;
    private boolean mClearingFocus;
    private FrameLayout mSearchLayout;
    private CharSequence mOldQueryText;
    private CharSequence mUserQuery;
    private ListAdapter mAdapter;
    private Drawable suggestionIcon;

    private boolean submit = false;
    private boolean ellipsize = false;

    private SearchView.OnQueryTextListener mOnQueryChangeListener;
    private SearchViewListener mSearchViewListener;
    private QrClickListener mQrClickListener;
    private SavedState mSavedState;

    public MaterialSearchView(Context context) {
        this(context, null);
    }

    public MaterialSearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        mContext = context;
        initView();
        initStyle(attrs, defStyleAttr);
        initSearchView();
    }


    public void setOnQueryChangeListener(SearchView.OnQueryTextListener onQueryChangeListener) {
        mOnQueryChangeListener = onQueryChangeListener;
    }

    public void setSearchViewListener(SearchViewListener searchViewListener) {
        mSearchViewListener = searchViewListener;
    }


    private void initSearchView() {
        mSearchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        mSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserQuery = s;
                startFilter(s);
                MaterialSearchView.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSearchTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(mSearchTextView);
                    showSuggestions();
                }
            }
        });
    }

    private void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager im = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }


    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showSuggestions() {
        if (mAdapter != null && mAdapter.getCount() > 0 && mSuggestionListView.getVisibility() == GONE) {
            mSuggestionListView.setVisibility(VISIBLE);
        }
    }


    public void setSuggestions(String[] suggestions) {
        if (suggestions != null && suggestions.length > 0) {
            mTintView.setVisibility(VISIBLE);
            final SearchAdapter searchAdapter = new SearchAdapter(mContext, suggestions, suggestionIcon, ellipsize);
            setAdapter(searchAdapter);
            setOnHintClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    setQuery((String) searchAdapter.getItem(position), submit);
                }
            });

        } else {
            mSuggestionListView.setVisibility(GONE);
        }
    }


    private void setOnHintClickListener(AdapterView.OnItemClickListener onHintClickListener) {
        mSuggestionListView.setOnItemClickListener(onHintClickListener);
    }

    private void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;
        mSuggestionListView.setAdapter(mAdapter);
        startFilter(mSearchTextView.getText());
    }


    private void onTextChanged(CharSequence s) {
        CharSequence text = mSearchTextView.getText();
        mUserQuery = text;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mClearBtn.setVisibility(View.VISIBLE);
            mClearBtn.dispatchSystemUiVisibilityChanged(VISIBLE);
            showVoice(false);
        } else {
            mClearBtn.setVisibility(GONE);
        }

        if (mOnQueryChangeListener != null && !TextUtils.equals(s, mOldQueryText)) {
            mOnQueryChangeListener.onQueryTextChange(s.toString());
        }
        mOldQueryText = s.toString();
    }

    private void showVoice(boolean show) {

    }

    private void startFilter(CharSequence s) {
        if (mAdapter != null && mAdapter instanceof Filterable) {
            ((Filterable) mAdapter).getFilter().filter(s, MaterialSearchView.this);
        }
    }

    private void onSubmitQuery() {
        CharSequence query = mSearchTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0) {
            if (mOnQueryChangeListener == null || !mOnQueryChangeListener.onQueryTextSubmit(query.toString())) {
                closeSearch();
                mSearchTextView.setText(null);
            }
        }
    }


    public void setEllipsize(boolean ellipsize) {
        this.ellipsize = ellipsize;
    }


    /**
     * Submit the query as soon as a user clicks the hint item
     *
     * @param submit true if submit immediately
     */
    public void setSubmit(boolean submit) {
        this.submit = submit;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.mMenuItem = menuItem;
        mMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showSearch();
                return true;
            }
        });
    }


    public void showSearch() {
        showSearch(true);
    }

    private void showSearch(boolean animate) {
        if (isSearchOpen()) return;
        mSearchTextView.setText(null);
        mSearchTextView.requestFocus();
        if (animate) setVisibleWithAnimation();
        else {
            mSearchLayout.setVisibility(VISIBLE);
            if (mSearchViewListener != null) {
                mSearchViewListener.onSearchViewShown();
            }
        }
        mIsSearchOpen = true;
    }


    private void setVisibleWithAnimation() {
        AnimationUtil.AnimationListener animationListener = new AnimationUtil.AnimationListener() {
            @Override
            public boolean onAnimationStart(View view) {
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewStartShow();
                }
                return false;
            }

            @Override
            public boolean onAnimationEnd(View view) {
                if (mSearchViewListener != null) {
                    mSearchViewListener.onSearchViewShown();
                }
                return false;
            }

            @Override
            public boolean onAnimationCancel(View view) {
                return false;
            }
        };

        AnimationUtil.fadeInView(mSearchLayout, mAnimationDuration, animationListener);
    }

    public void closeSearch() {
        if (!isSearchOpen()) {
            return;
        }
        mSearchTextView.setText(null);
        dismissSuggestions();
        clearFocus();
        mSearchLayout.setVisibility(GONE);
        if (mSearchViewListener != null) {
            mSearchViewListener.onSearchViewClosed();
        }
        mIsSearchOpen = false;
    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (mClearingFocus) return false;
        if (!isFocusable()) return false;
        return mSearchTextView.requestFocus(direction, previouslyFocusedRect);
    }


    @Override
    public void clearFocus() {
        mClearingFocus = true;
        hideKeyboard(this);
        super.clearFocus();
        mSearchTextView.clearFocus();
        mClearingFocus = false;
    }

    //TODO: dismiss suggestions listView
    private void dismissSuggestions() {
        mSuggestionListView.setVisibility(View.GONE);
        mSuggestionListView.clearFocus();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.search_view, this, true);
        mSearchLayout = (FrameLayout) findViewById(R.id.search_layout);
        mSearchTopBar = (RelativeLayout) mSearchLayout.findViewById(R.id.search_bar);
        mSearchTextView = (EditText) mSearchLayout.findViewById(R.id.search_text);
        mBackBtn = (ImageButton) mSearchLayout.findViewById(R.id.btn_action_close);
        mQrBtn = (ImageButton) mSearchLayout.findViewById(R.id.btn_action_scan);
        mClearBtn = (ImageButton) mSearchLayout.findViewById(R.id.btn_action_clear);
        mTintView = mSearchLayout.findViewById(R.id.view_transparent);

        mSuggestionListView = (ListView) mSearchLayout.findViewById(R.id.suggestions_list);
        mSuggestionListView.setVisibility(GONE);
        mQrBtn.setOnClickListener(mOnClickListener);
        mSearchTextView.setOnClickListener(mOnClickListener);
        mBackBtn.setOnClickListener(mOnClickListener);
        mClearBtn.setOnClickListener(mOnClickListener);
        mTintView.setOnClickListener(mOnClickListener);


        setAnimationDuration(AnimationUtil.ANIMATION_DURATION_MEDIUM);

    }


    public void setSuggestionBackground(@DrawableRes Drawable drawable) {

    }

    public void setSuggestionIcon(@DrawableRes Drawable drawable) {
        suggestionIcon = drawable;
    }


    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSearchTopBar.setBackground(background);
        } else {
            mSearchTopBar.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color) {
        mSearchTopBar.setBackgroundColor(color);
    }

    public void setTextColor(@ColorInt int color) {
        mSearchTextView.setTextColor(color);
    }

    public void setHintTextColor(@ColorInt int color) {
        mSearchTextView.setHintTextColor(color);
    }

    public void setHint(CharSequence hint) {
        mSearchTextView.setHint(hint);
    }

    public void setClearIcon(@DrawableRes Drawable drawable) {
        mClearBtn.setImageDrawable(drawable);
    }

    public void setBackIcon(@DrawableRes Drawable drawable) {
        mBackBtn.setImageDrawable(drawable);
    }

    public void setQrIcon(@DrawableRes Drawable drawable) {
        mQrBtn.setImageDrawable(drawable);
    }

    public void setAnimationDuration(int animationDuration) {
        this.mAnimationDuration = animationDuration;
    }

    private void initStyle(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.MaterialSearchView, defStyleAttr, 0);
        if (a != null) {
            if (a.hasValue(R.styleable.MaterialSearchView_searchBackground)) {
                setBackground(a.getDrawable(R.styleable.MaterialSearchView_searchBackground));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_android_textColor)) {
                setTextColor(a.getColor(R.styleable.MaterialSearchView_android_textColor, 0));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_android_hint)) {
                setHint(a.getString(R.styleable.MaterialSearchView_android_hint));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_android_textColorHint)) {
                setHintTextColor(a.getColor(R.styleable.MaterialSearchView_android_textColorHint, 0));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchBackIcon)) {
                setBackIcon(a.getDrawable(R.styleable.MaterialSearchView_searchBackIcon));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchCloseIcon)) {
                setClearIcon(a.getDrawable(R.styleable.MaterialSearchView_searchCloseIcon));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchSuggestionBackground)) {
                setSuggestionBackground(a.getDrawable(R.styleable.MaterialSearchView_searchSuggestionBackground));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchSuggestionIcon)) {
                setSuggestionIcon(a.getDrawable(R.styleable.MaterialSearchView_searchSuggestionIcon));
            }

            if (a.hasValue(R.styleable.MaterialSearchView_searchQrIcon)) {
                setQrIcon(a.getDrawable(R.styleable.MaterialSearchView_searchQrIcon));
            }

            a.recycle();
        }
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mBackBtn) {
                closeSearch();
            } else if (v == mClearBtn) {
                mSearchTextView.setText(null);
            } else if (v == mSearchTextView) {
                showSuggestions();
            } else if (v == mQrBtn) {
                if (mQrClickListener != null) {
                    mQrClickListener.onScanClicked();
                }
            } else if (v == mTintView) {
                closeSearch();
            }
        }
    };


    public void setQrClickListener(QrClickListener qrClickListener) {
        mQrClickListener = qrClickListener;
    }

    public interface QrClickListener {
        void onScanClicked();
    }


    @Override
    public void onFilterComplete(int count) {
        if (count > 0) showSuggestions();
        else dismissSuggestions();
    }

    public boolean isSearchOpen() {
        return mIsSearchOpen;
    }

    public interface SearchViewListener {
        void onSearchViewShown();

        void onSearchViewClosed();

        void onSearchViewStartShow();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        mSavedState = (SavedState) state;
        if (mSavedState.open) {
            showSearch(false);
            setQuery(mSavedState.currentQuery, false);
        }
        super.onRestoreInstanceState(mSavedState.getSuperState());

    }

    public void setQuery(CharSequence query, boolean submit) {
        mSearchTextView.setText(query);
        if (query != null) {
            mSearchTextView.setSelection(mSearchTextView.length());
            mUserQuery = query;
        }
        if (submit && !TextUtils.isEmpty(query)) {
            onSubmitQuery();
        }
    }

    //Save the current state of view
    static class SavedState extends BaseSavedState {
        String currentQuery;
        boolean open;

        public SavedState(Parcel source) {
            super(source);
            this.currentQuery = source.readString();
            this.open = source.readInt() == 1;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(currentQuery);
            out.writeInt(open ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
