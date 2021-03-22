package com.livetv.normal.view;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Process;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.livetv.normal.R;
import com.livetv.normal.databinding.MainCategoriesMenuFragmentBinding;
import com.livetv.normal.model.MainCategory;
import com.livetv.normal.model.ModelTypes.SelectedType;
import com.livetv.normal.utils.DataManager;
import com.livetv.normal.utils.Device;
import com.livetv.normal.viewmodel.Lifecycle;
import com.livetv.normal.viewmodel.MainCategoriesMenuViewModel;
import com.livetv.normal.viewmodel.MainCategoriesMenuViewModelContract;

public class MainCategoriesMenuFragment extends BaseFragment implements MainCategoriesMenuViewModelContract.View {
    private MainCategoriesMenuFragmentBinding mainCategoriesMenuFragmentBinding;
    private MainCategoriesMenuViewModel mainCategoriesMenuViewModel;
    private String password = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainCategoriesMenuViewModel = new MainCategoriesMenuViewModel(getContext());
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged() {
        this.mainCategoriesMenuViewModel.onConfigurationChanged();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mainCategoriesMenuFragmentBinding = (MainCategoriesMenuFragmentBinding) DataBindingUtil.inflate(inflater, R.layout.main_categories_menu_fragment, container, false);
        this.mainCategoriesMenuFragmentBinding.setMainCategoriesMenuFragmentVM((MainCategoriesMenuViewModel) getViewModel());
        ((AppCompatActivity) getActivity()).setSupportActionBar(this.mainCategoriesMenuFragmentBinding.toolbar);
        this.mainCategoriesMenuFragmentBinding.account.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setSelected(true);
                } else {
                    v.setSelected(false);
                }
            }
        });
        if (Device.treatAsBox) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        this.mainCategoriesMenuViewModel.showMainCategories(this.mainCategoriesMenuFragmentBinding.mainCategoriesRecyclerview);
        return this.mainCategoriesMenuFragmentBinding.getRoot();
    }

    /* access modifiers changed from: protected */
    public Lifecycle.ViewModel getViewModel() {
        return this.mainCategoriesMenuViewModel;
    }

    /* access modifiers changed from: protected */
    public Lifecycle.View getLifecycleView() {
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return false;
        }
        Process.killProcess(Process.myPid());
        return true;
    }

    public void onMainCategorySelected(MainCategory mainCategory) {
        int mainCategoryId = mainCategory.getId();
        if (mainCategoryId == 8) {
            onAccountPressed();
        } else if (mainCategoryId != 7) {
            Bundle extras = new Bundle();
            extras.putSerializable("selectedType", SelectedType.MAIN_CATEGORY);
            extras.putInt("mainCategoryId", mainCategory.getId());
            launchActivity(LoadingMoviesActivity.class, extras);
        } else if (TextUtils.isEmpty(DataManager.getInstance().getString("adultsPassword", ""))) {
            openSetPasswordDialog();
        } else {
            openPasswordDialog();
        }
    }

    public void onAccountPressed() {
        launchActivity(AccountDetailsActivity.class);
    }

    private void openSetPasswordDialog() {
        Builder alert = new Builder(getContext(), R.style.AppThemeDialog);
        final EditText edittext = new EditText(getContext());
        edittext.setRawInputType(8194);
        alert.setMessage((int) R.string.adults_set_password_message);
        alert.setTitle((int) R.string.prompt_password);
        alert.setView((android.view.View) edittext);
        alert.setPositiveButton((int) R.string.accept, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String input = edittext.getText().toString();
                if (TextUtils.isEmpty(input) || input.length() < 4) {
                    Toast.makeText(MainCategoriesMenuFragment.this.getContext(), R.string.adults_set_password_empty_message, Toast.LENGTH_LONG).show();
                    return;
                }
                DataManager.getInstance().saveData("adultsPassword", input);
                Toast.makeText(MainCategoriesMenuFragment.this.getContext(), R.string.adults_set_password_success_message, Toast.LENGTH_LONG).show();
                MainCategoriesMenuFragment.this.openPasswordDialog();
            }
        });
        alert.setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    /* access modifiers changed from: private */
    public void openPasswordDialog() {
        Builder alert = new Builder(getContext(), R.style.AppThemeDialog);
        final EditText edittext = new EditText(getContext());
        edittext.setRawInputType(8194);
        alert.setMessage((int) R.string.adults_password_message);
        alert.setTitle((int) R.string.prompt_password);
        alert.setView((android.view.View) edittext);
        alert.setPositiveButton((int) R.string.accept, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (edittext.getText().toString().equals(DataManager.getInstance().getString("adultsPassword", ""))) {
                    Bundle extras = new Bundle();
                    extras.putSerializable("selectedType", SelectedType.MAIN_CATEGORY);
                    extras.putInt("mainCategoryId", 7);
                    MainCategoriesMenuFragment.this.launchActivity(LoadingMoviesActivity.class, extras);
                    return;
                }
                dialog.dismiss();
                Toast.makeText(MainCategoriesMenuFragment.this.getContext(), R.string.error_invalid_password, Toast.LENGTH_LONG).show();
            }
        });
        alert.setNegativeButton((int) R.string.cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alert.create();
        dialog.create();
        dialog.show();
        Button ne=dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button po=dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        ne.setBackground(getResources().getDrawable(R.drawable.dialog_btn_background));
        po.setBackground(getResources().getDrawable(R.drawable.dialog_btn_background));
        ne.setPadding(16,4,16,4);
        po.setPadding(16,4,16,4);
    }
}
