package br.edu.ifsp.sbv.monstropedia;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sbv.monstropedia.modelo.HttpUtils;
import br.edu.ifsp.sbv.monstropedia.modelo.Monstro;
import br.edu.ifsp.sbv.monstropedia.modelo.MonstroDAO;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog load;
    private TextInputEditText edNomeMonstro;
    private ListView lvMonstros;
    private MonstroDAO dao;
    private List<Monstro> listaMonstros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edNomeMonstro = (TextInputEditText) findViewById(R.id.edNomeMonster);
        lvMonstros = (ListView) findViewById(R.id.lvMonstros);
        
        dao = new MonstroDAO(this);
        listaMonstros = new ArrayList<>();

        lvMonstros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exibirDetalhesMonstro(listaMonstros.get(position));
            }
        });
        lvMonstros.setOnItemLongClickListener(excluirMonstroListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        atualizarLista();
    }

    private void exibirDetalhesMonstro(Monstro monstro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.monstro_info, null);
        builder.setView(dialogView);

        TextView txtNome = dialogView.findViewById(R.id.txtName);
        TextView txtSizeTypeAlign = dialogView.findViewById(R.id.txtSizeTypeAlign);
        TextView txtACvalue = dialogView.findViewById(R.id.txtACvalue);
        TextView txtHPvalue = dialogView.findViewById(R.id.txtHPvalue);
        TextView txtSPDvalue = dialogView.findViewById(R.id.txtSPDvalue);
        TextView txtStr = dialogView.findViewById(R.id.txtStr);
        TextView txtDex = dialogView.findViewById(R.id.txtDex);
        TextView txtCon = dialogView.findViewById(R.id.txtCon);
        TextView txtIntl = dialogView.findViewById(R.id.txtIntl);
        TextView txtWis = dialogView.findViewById(R.id.txtWis);
        TextView txtCha = dialogView.findViewById(R.id.txtCha);
        TextView txtSavingThrow = dialogView.findViewById(R.id.txtSavingThrow);
        TextView txtSkill = dialogView.findViewById(R.id.txtSkill);
        TextView txtDamageImun = dialogView.findViewById(R.id.txtDamageImun);
        TextView txtSenses = dialogView.findViewById(R.id.txtSenses);
        TextView txtLanguages = dialogView.findViewById(R.id.txtLanguages);
        TextView txtChallenge = dialogView.findViewById(R.id.txtChallenge);
        ImageView ivMonstro = dialogView.findViewById(R.id.ivMonster);

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

        if (monstro.getImage() != null) {
            new DownloadImageTask(ivMonstro).execute(monstro.getImage());
        }

        builder.setPositiveButton("Fechar", null);
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.background);
        }
    }

    public void pesquisar(View view) {
        String nome = edNomeMonstro.getText().toString();
        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            return;
        }
        new fetchMonsterTask().execute(nome);
    }

    private void atualizarLista() {
        listaMonstros = dao.listAll();
        MonstroListAdapter adapter = new MonstroListAdapter(this, listaMonstros);
        lvMonstros.setAdapter(adapter);
    }

    private AdapterView.OnItemLongClickListener excluirMonstroListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Monstro monstro = listaMonstros.get(position);
            
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Excluir monstro?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Deseja excluir " + monstro.getName() + "?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (dao.deletar(monstro.getName())) {
                                atualizarLista();
                                Toast.makeText(MainActivity.this, "Monstro excluído!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Erro ao excluir!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Não", null);
            builder.create().show();
            return true;
        }
    };

    public class fetchMonsterTask extends AsyncTask<String, Void, Monstro> {
        private static final String TAG = "FetchMonsterTask";

        protected void onPreExecute() {
            load = ProgressDialog.show(MainActivity.this, "Por favor Aguarde ...",
                    "Procurando Dados ...");
        }

        @Override
        protected Monstro doInBackground(String... params) {
            String nomeBusca = params[0];
            
            // Verifica no banco interno
            Monstro monstro = dao.findByName(nomeBusca);
            
            if (monstro == null) {
                // Se não estiver no banco, busca na API
                String nomeMonstroApi = nomeBusca.toLowerCase().replace(" ","-");
                String urlString = "https://www.dnd5eapi.co/api/2014/monsters/" + nomeMonstroApi;

                try {
                    String jsonString = HttpUtils.get(urlString);
                    JSONObject jsonObject = new JSONObject(jsonString);
                    monstro = new Monstro(jsonObject);
                    
                    // Salva no banco interno
                    dao.insert(monstro);
                } catch (Exception e) {
                    Log.e(TAG, "Erro na requisição ou inserção: ", e);
                }
            }
            return monstro;
        }

        @Override
        protected void onPostExecute(Monstro monstro) {
            load.dismiss();
            if (monstro != null) {
                Toast.makeText(MainActivity.this, "Monstro: " + monstro.getName(), Toast.LENGTH_LONG).show();
                atualizarLista();
                edNomeMonstro.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Nenhum monstro encontrado!", Toast.LENGTH_LONG).show();
            }
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
