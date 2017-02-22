package com.teamappjobs.appjobs.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.activity.CadastroUsuarioActivity;
import com.teamappjobs.appjobs.activity.LoginActivity;
import com.teamappjobs.appjobs.asyncTask.EditaUsuarioTask;
import com.teamappjobs.appjobs.modelo.Usuario;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Lucas on 15/02/2017.
 */

public class CadastroComplementarFirebase extends Fragment {

    private TextView txtBemVindo;
    private static EditText txtDataNasc;
    private EditText txtTelefone;
    private RadioGroup rbSexo;
    private RadioButton rbMasc;
    private RadioButton rbFem;
    private FloatingActionButton fabCadastrar;
    private CoordinatorLayout coordinatorLayout;
    private CircleImageView imagePerfil;

    private Usuario usuario;

    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    DateFormat brDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       usuario = ((LoginActivity)getActivity()).getUsuarioLogado();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_cadastro_firebase, container,false);

        coordinatorLayout = (CoordinatorLayout) fragment.findViewById(R.id.coordinator);
        imagePerfil = (CircleImageView) fragment.findViewById(R.id.imagePerfil);
        txtTelefone = (EditText) fragment.findViewById(R.id.txtTelefone);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            txtTelefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("BR"));
        }
        txtDataNasc = (EditText) fragment.findViewById(R.id.txtDataNasc);
        txtDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dataFragment = new CadastroComplementarFirebase.DatePickerFragment();
                dataFragment.show(getChildFragmentManager(), "dataNasc");
            }
        });
        rbSexo = (RadioGroup) fragment.findViewById(R.id.radiogroupsex);
        rbFem=(RadioButton) fragment.findViewById(R.id.radiobuttonfem);
        rbMasc=(RadioButton) fragment.findViewById(R.id.radiobuttonmasc);

        txtBemVindo = (TextView) fragment.findViewById(R.id.txtBemVindoXX);

        fabCadastrar = (FloatingActionButton) fragment.findViewById(R.id.fabCadastrar);
        fabCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cadastrar
                if(validaCampos()){

                    //Tratar RadioButton
                    String sexo;
                    switch (rbSexo.getCheckedRadioButtonId()) {
                        case R.id.radiobuttonmasc:
                            sexo = "masculino";
                            usuario.setSexo(sexo);
                            break;
                        case R.id.radiobuttonfem:
                            sexo = "feminino";
                            usuario.setSexo(sexo);
                            break;
                    }

                    //Tratar telefone
                    List<String> tel = new ArrayList<String>();
                    tel.add(txtTelefone.getText().toString());
                    usuario.setTelefone(tel);

                    //Tratar DataNasc
                    try {
                        usuario.setDataNasc(brDateFormat.parse(txtDataNasc.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    EditaUsuarioTask task = new EditaUsuarioTask(getActivity(),usuario, null,true);
                    task.execute();

                }
            }
        });
        preencheCampos();

        return fragment;
    }


    public void preencheCampos(){
        Picasso.with(getActivity()).load(getResources().getString(R.string.imageserver) + usuario.getImagemPerfil()).into(imagePerfil);
        String msg = getActivity().getResources().getString(R.string.bemVindo_)+ " " + usuario.getNome();
        txtBemVindo.setText(msg);
    }

    public Boolean validaCampos() {
        Boolean retorno = true;
        if (txtDataNasc.getText().toString().equals("")) {
            txtDataNasc.setError(getResources().getString(R.string.preenchacampo));
            txtDataNasc.setFocusable(true);
            retorno = false;
        }
        if (rbSexo.getCheckedRadioButtonId() == View.NO_ID) {
            rbSexo.setFocusable(true);
            Snackbar.make(coordinatorLayout,R.string.sexonaopreenchido,Snackbar.LENGTH_SHORT).show();
            retorno = false;
        }
        if (txtTelefone.getText().toString().equals("")) {
            txtTelefone.setError(getResources().getString(R.string.preenchacampo));
            txtTelefone.setFocusable(true);
            retorno = false;
        }
        return retorno;
    }





    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
            return picker;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            String txtmes = "";
            String txtdia = "";

            month = month + 1;

            if (month < 10) {
                txtmes = "0" + String.valueOf(month);
            } else {
                txtmes = String.valueOf(month);
            }
            if (day < 10) {
                txtdia = "0" + String.valueOf(day);
            } else {
                txtdia = String.valueOf(day);
            }

            String aux = txtdia + "/" + txtmes + "/" + year;
            txtDataNasc.setText(aux);

        }
    }

}
