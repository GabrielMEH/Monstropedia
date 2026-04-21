package br.edu.ifsp.sbv.monstropedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.sbv.monstropedia.modelo.Monstro;

public class MonstroListAdapter extends BaseAdapter {

    private Context context;
    private List<Monstro> lista;

    public MonstroListAdapter(Context context, List<Monstro> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Monstro m = lista.get(position);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.monstros, parent, false);
        }

        TextView nome = (TextView) convertView.findViewById(R.id.txtNomeMonstroLinha);
        nome.setText(m.getName());

        TextView cr = (TextView) convertView.findViewById(R.id.txtCRMonstroLinha);
        String valorCR = "CR: " + String.valueOf(m.getCR());
        cr.setText(valorCR);

        ImageView icone = (ImageView) convertView.findViewById(R.id.imgIconeMonstro);
        int resId = getIconId(m.getType().toLowerCase());
        if (resId != 0) {
            icone.setImageResource(resId);
        } else {
            icone.setImageResource(R.drawable.hunt_quest_icon);
        }

        return convertView;
    }

    private int getIconId(String type) {
        String cleanType = type.split(" ")[0].replaceAll("[^a-z]", "");
        
        switch (cleanType) {
            case "aberration": return R.drawable.aberration;
            case "beast": return R.drawable.beast;
            case "celestial": return R.drawable.celestial;
            case "construct": return R.drawable.construct;
            case "dragon": return R.drawable.dragon;
            case "elemental": return R.drawable.elemental;
            case "fey": return R.drawable.fey;
            case "fiend": return R.drawable.fiend;
            case "giant": return R.drawable.giant;
            case "humanoid": return R.drawable.humanoid;
            case "monstrosity": return R.drawable.monstrosity;
            case "ooze": return R.drawable.ooze;
            case "plant": return R.drawable.plant;
            case "undead": return R.drawable.undead;
            default: return 0;
        }
    }
}
