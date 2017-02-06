package com.teamappjobs.appjobs.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.teamappjobs.appjobs.R;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePortifolioFragment;
import com.teamappjobs.appjobs.fragment.MinhaVitrinePromocoesFragment;
import com.teamappjobs.appjobs.modelo.Portifolio;
import com.teamappjobs.appjobs.modelo.Promocao;
import com.teamappjobs.appjobs.util.SlideshowDialogFragment;
import com.teamappjobs.appjobs.web.HttpConnection;
import com.teamappjobs.appjobs.web.VitrineJson;

/**
 * Created by Lucas on 13/05/2016.
 */
public class DeletaPortifolioTask extends AsyncTask {
    private final String url = "http://www.4runnerapp.com.br/AppJobs/Ctrl/cadastra_portifolio_json.php";
    private final String method = "deleta_portifolio-json";
    private ProgressDialog progress;

    private Context context;
    private Portifolio portifolio;
    private Fragment fragment;
    private int positionOnPager;

    public DeletaPortifolioTask(Context context, Portifolio portifolio) {
        this.context = context;
        this.portifolio = portifolio;
    }

    public DeletaPortifolioTask(Context context, Portifolio portifolio, Fragment fragment, int positionOnPager) {
        this.context = context;
        this.portifolio = portifolio;
        this.fragment = fragment;
        this.positionOnPager = positionOnPager;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, "Aguarde...", "Envio de dados para web", true, true);
    }

    @Override
    protected Object doInBackground(Object[] params) {

        VitrineJson json = new VitrineJson();
        String jsonPortifolio = json.PortifolioToJson(portifolio);

        String answer = HttpConnection.getSetDataWeb(this.url, this.method, jsonPortifolio);
        Log.i("RespDelPortif", answer);
        return answer;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progress.dismiss();

        if (o.equals("Sucesso")) {

            Toast.makeText(context, R.string.promoCancelada, Toast.LENGTH_SHORT).show();
            if (fragment != null && fragment instanceof MinhaVitrinePortifolioFragment) {
                ((MinhaVitrinePortifolioFragment) fragment).listaPortifolio();
            }
            if (fragment != null && fragment instanceof SlideshowDialogFragment) {
                ((SlideshowDialogFragment) fragment).recarregarLista(positionOnPager, portifolio);
            }

        } else {

            Toast.makeText(context, R.string.opserro, Toast.LENGTH_SHORT).show();
        }


    }

}
