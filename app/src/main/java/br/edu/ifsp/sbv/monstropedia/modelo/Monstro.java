package br.edu.ifsp.sbv.monstropedia.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class Monstro {
    //geral
    String name;
    String size;
    String type;
    String align;
    int AC;
    String ACtype;
    int HP;
    String HProll;

    //walk, fly e swim
    String speed;

    //stats
    int str;
    int dex;
    int con;
    int intl;
    int wis;
    int cha;

    //
    String saving_throw;
    String skill;
    String damage_vuln;
    String damage_resi;
    String damage_imun;
    String condition_imun;
    String senses;
    String languages;
    int CR;
    int XP;

    //
    private String image;

    public Monstro(JSONObject json) throws JSONException {
        this.name = json.getString("name");
        this.size = json.getString("size");
        this.type = json.getString("type");
        this.align = json.getString("alignment");
        this.AC = json.getJSONArray("armor_class").getJSONObject(0).getInt("value");
        this.ACtype = json.getJSONArray("armor_class").getJSONObject(0).getString("type");
        this.HP = json.getInt("hit_points");
        this.HProll = json.getString("hit_points_roll");
        this.speed = getSpeed(json.getJSONObject("speed"));
        this.str = json.getInt("strength");
        this.dex = json.getInt("dexterity");
        this.con = json.getInt("constitution");
        this.intl = json.getInt("intelligence");
        this.wis = json.getInt("wisdom");
        this.cha = json.getInt("charisma");

        this.saving_throw = getSavingThrow(json.getJSONArray("proficiencies"));
        this.skill = getSkill(json.getJSONArray("proficiencies"));

        this.damage_vuln = getDamage_vuln(json.getJSONArray("damage_vulnerabilities"));
        this.damage_resi = getDamage_resi(json.getJSONArray("damage_resistances"));
        this.damage_imun = getDamage_imun(json.getJSONArray("damage_immunities"));
        this.condition_imun = getConditionimun(json.getJSONArray("condition_immunities"));

        this.senses = getSenses(json.getJSONObject("senses"));
        this.languages = json.getString("languages");
        this.CR = json.getInt("challenge_rating");
        this.XP = json.getInt("xp");

        this.image = json.optString("image", null);
    }

    private String getSpeed(JSONObject json) throws JSONException {
        StringBuilder speed = new StringBuilder();
        Iterator<String> keys = json.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (speed.length() > 0) speed.append(", ");
            if (!key.equals("walk")) {
                speed.append(key).append(" ");
            }
            speed.append(json.getString(key));
        }
        return speed.toString();
    }

    private String getSavingThrow(JSONArray proficiencies) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < proficiencies.length(); i++) {
            JSONObject entry = proficiencies.getJSONObject(i);
            JSONObject prof = entry.getJSONObject("proficiency");
            String index = prof.getString("index");
            if (index.startsWith("saving-throw-")) {
                if (sb.length() > 0) sb.append(", ");
                String name = prof.getString("name").replace("Saving Throw: ", "");
                sb.append(name).append(" +").append(entry.getInt("value"));
            }
        }
        return sb.toString();
    }

    private String getSkill(JSONArray proficiencies) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < proficiencies.length(); i++) {
            JSONObject entry = proficiencies.getJSONObject(i);
            JSONObject prof = entry.getJSONObject("proficiency");
            String index = prof.getString("index");
            if (index.startsWith("skill-")) {
                if (sb.length() > 0) sb.append(", ");
                String name = prof.getString("name").replace("Skill: ", "");
                sb.append(name).append(" +").append(entry.getInt("value"));
            }
        }
        return sb.toString();
    }

    private String getDamage_vuln(JSONArray json) throws JSONException {
        return joinJsonArray(json);
    }

    private String getDamage_resi(JSONArray json) throws JSONException {
        return joinJsonArray(json);
    }

    private String getDamage_imun(JSONArray json) throws JSONException {
        return joinJsonArray(json);
    }

    private String getConditionimun(JSONArray json) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < json.length(); i++) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(json.getJSONObject(i).getString("name"));
        }
        return sb.toString();
    }

    private String getSenses(JSONObject json) throws JSONException {
        StringBuilder sb = new StringBuilder();
        Iterator<String> keys = json.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (sb.length() > 0) sb.append(", ");
            String name = key.replace("_", " ");
            sb.append(name).append(" ").append(json.get(key));
        }
        return sb.toString();
    }

    private String joinJsonArray(JSONArray jsonArray) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(jsonArray.getString(i));
        }
        return sb.toString();
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSize() { return size; }

    public void setSize(String size) { this.size = size; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getAlign() { return align; }

    public void setAlign(String align) { this.align = align; }

    public int getAC() { return AC; }

    public void setAC(int AC) { this.AC = AC; }

    public String getACtype() { return ACtype; }

    public void setACtype(String ACtype) { this.ACtype = ACtype; }

    public String getACACtype() {
        String ac = String.valueOf(this.AC) + " (" + this.ACtype + ")";
        return ac;
    }

    public int getHP() { return HP; }

    public void setHP(int HP) { this.HP = HP; }

    public String getHProll() { return HProll; }

    public void setHProll(String HProll) { this.HProll = HProll; }

    public String getHPHProll() {
        String hp = String.valueOf(this.HP) + " (" + this.HProll + ")";
        return hp;
    }

    public String getSpeed() { return speed; }

    public void setSpeed(String speed) { this.speed = speed; }

    public int getStr() { return str; }

    public String getStr2() {
        String mod = String.valueOf((this.str-10)/2);
        String str = String.valueOf(this.str) + " (" + mod + ")";
        return str;
    }

    public void setStr(int str) { this.str = str; }

    public int getDex() { return dex; }

    public String getDex2() {
        String mod = String.valueOf((this.dex-10)/2);
        String str = String.valueOf(this.dex) + " (" + mod + ")";
        return str;
    }

    public void setDex(int dex) { this.dex = dex; }

    public int getCon() { return con; }

    public String getCon2() {
        String mod = String.valueOf((this.con-10)/2);
        String str = String.valueOf(this.con) + " (" + mod + ")";
        return str;
    }

    public void setCon(int con) { this.con = con; }

    public int getIntl() { return intl; }

    public String getIntl2() {
        String mod = String.valueOf((this.intl-10)/2);
        String str = String.valueOf(this.intl) + " (" + mod + ")";
        return str;
    }

    public void setIntl(int intl) { this.intl = intl; }

    public int getWis() { return wis; }

    public String getWis2() {
        String mod = String.valueOf((this.wis-10)/2);
        String str = String.valueOf(this.wis) + " (" + mod + ")";
        return str;
    }

    public void setWis(int wis) { this.wis = wis; }

    public int getCha() { return cha; }

    public String getCha2() {
        String mod = String.valueOf((this.cha-10)/2);
        String str = String.valueOf(this.cha) + " (" + mod + ")";
        return str;
    }

    public void setCha(int cha) { this.cha = cha; }

    public String getSaving_throw() { return saving_throw; }

    public void setSaving_throw(String saving_throw) { this.saving_throw = saving_throw; }

    public String getSkill() { return skill; }

    public void setSkill(String skill) { this.skill = skill; }

    public String getDamage_vuln() { return damage_vuln; }

    public void setDamage_vuln(String damage_vuln) { this.damage_vuln = damage_vuln; }

    public String getDamage_resi() { return damage_resi; }

    public void setDamage_resi(String damage_resi) { this.damage_resi = damage_resi; }

    public String getDamage_imun() { return damage_imun; }

    public void setDamage_imun(String damage_imun) { this.damage_imun = damage_imun; }

    public String getCondition_immunities() { return condition_imun; }

    public void setCondition_immunities(String condition_immunities) { this.condition_imun = condition_immunities; }

    public String getSenses() { return senses; }

    public void setSenses(String senses) { this.senses = senses; }

    public String getLanguages() { return languages; }

    public void setLanguages(String languages) { this.languages = languages; }

    public int getCR() { return CR; }

    public void setCR(int CR) { this.CR = CR; }

    public int getXP() { return XP; }

    public void setXP(int XP) { this.XP = XP; }

    public String getCRXP() {
        String cr = String.valueOf(this.CR) + " (" + this.XP + " XP)";
        return cr;
    }

    public String getImage() {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return "https://www.dnd5eapi.co" + image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSizeTypeAlign() {
        return this.size + " " + this.type + ", " + this.align;
    }
}
