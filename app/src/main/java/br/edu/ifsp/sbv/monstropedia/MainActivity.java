package br.edu.ifsp.sbv.monstropedia;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

import br.edu.ifsp.sbv.monstropedia.modelo.HttpUtils;
import br.edu.ifsp.sbv.monstropedia.modelo.Monstro;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog load;

    TextInputEditText edNomeMonstro;
    TextView txtNome;
    TextView txtSizeTypeAlign;
    TextView txtACvalue;
    TextView txtHPvalue;
    TextView txtSPDvalue;
    TextView txtStr;
    TextView txtDex;
    TextView txtCon;
    TextView txtIntl;
    TextView txtWis;
    TextView txtCha;
    TextView txtSavingThrow;
    TextView txtSkill;
    TextView txtDamageImun;
    TextView txtSenses;
    TextView txtLanguages;
    TextView txtChallenge;
    ImageView ivMonstro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        txtNome = (TextView) findViewById(R.id.txtName);
        txtSizeTypeAlign = (TextView) findViewById(R.id.txtSizeTypeAlign);
        txtACvalue = (TextView) findViewById(R.id.txtACvalue);
        txtHPvalue = (TextView) findViewById(R.id.txtHPvalue);
        txtSPDvalue = (TextView) findViewById(R.id.txtSPDvalue);
        txtStr = (TextView) findViewById(R.id.txtStr);
        txtDex = (TextView) findViewById(R.id.txtDex);
        txtCon = (TextView) findViewById(R.id.txtCon);
        txtIntl = (TextView) findViewById(R.id.txtIntl);
        txtWis = (TextView) findViewById(R.id.txtWis);
        txtCha = (TextView) findViewById(R.id.txtCha);
        txtSavingThrow = (TextView) findViewById(R.id.txtSavingThrow);
        txtSkill = (TextView) findViewById(R.id.txtSkill);
        txtDamageImun = (TextView) findViewById(R.id.txtDamageImun);
        txtSenses = (TextView) findViewById(R.id.txtSenses);
        txtLanguages = (TextView) findViewById(R.id.txtLanguages);
        txtChallenge = (TextView) findViewById(R.id.txtChallenge);
        edNomeMonstro = (TextInputEditText) findViewById(R.id.edNomeMonster);
        ivMonstro = (ImageView) findViewById(R.id.ivMonster);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void pesquisar (View view) {
        new fetchMonsterTask().execute(edNomeMonstro.getText().toString());
    }

    public class fetchMonsterTask extends AsyncTask<String, Void, Monstro> {
        private static final String TAG = "FetchMonsterTask";

        protected void onPreExecute() {
            load = ProgressDialog.show(MainActivity.this, "Por favor Aguarde ...",
                    "Procurando Dados ...");
        }

        @Override
        protected Monstro doInBackground(String... params) {

            String nomeMonstroBusca = params[0].toLowerCase().replace(" ","-");
            String urlString = "https://www.dnd5eapi.co/api/2014/monsters/"+nomeMonstroBusca;

            try {
                String jsonString = HttpUtils.get(urlString);
                JSONObject jsonObject = new JSONObject(jsonString);
                return new Monstro(jsonObject);
            } catch (Exception e) {
                Log.e(TAG, "Erro na requisição: ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Monstro monstro) {

            if(monstro !=null)
            {
                Toast.makeText(MainActivity.this,String.valueOf(monstro.getName()),Toast.LENGTH_LONG).show();
                txtNome.setText(monstro.getName());
                txtSizeTypeAlign.setText(monstro.getSizeTypeAlign());
                txtACvalue.setText(monstro.getACACtype());
                txtHPvalue.setText(monstro.getHPHProll());
                txtSPDvalue.setText(monstro.getSpeed());
                txtStr.setText(monstro.getStr2());
                txtDex.setText(monstro.getDex2());
                txtCon.setText(monstro.getCon2());
                txtIntl.setText(monstro.getIntl2());
                txtWis.setText(monstro.getWis2());
                txtCha.setText(monstro.getCha2());
                txtSavingThrow.setText(monstro.getSaving_throw());
                txtSkill.setText(monstro.getSkill());
                txtDamageImun.setText(monstro.getDamage_imun());
                txtSenses.setText(monstro.getSenses());
                txtLanguages.setText(monstro.getLanguages());
                txtChallenge.setText(monstro.getCRXP());
                if(monstro.getImage()!=null)
                {
                    new DownloadImageTask(ivMonstro)
                            .execute(monstro.getImage());
                }
            }
            else {
                Toast.makeText(MainActivity.this,"Nenhum monstro encontrado!",Toast.LENGTH_LONG).show();
                txtNome.setText("");
                txtSizeTypeAlign.setText("");
                txtACvalue.setText("");
                txtHPvalue.setText("");
                txtSPDvalue.setText("");
                txtStr.setText("");
                txtDex.setText("");
                txtCon.setText("");
                txtIntl.setText("");
                txtWis.setText("");
                txtCha.setText("");
                txtSavingThrow.setText("");
                txtSkill.setText("");
                txtDamageImun.setText("");
                txtSenses.setText("");
                txtLanguages.setText("");
                txtChallenge.setText("");
            }

            load.dismiss();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
