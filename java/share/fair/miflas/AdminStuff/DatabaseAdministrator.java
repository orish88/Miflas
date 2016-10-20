package share.fair.miflas.AdminStuff;

import com.firebase.client.authentication.Constants;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Ori on 9/29/2016.
 */
public class DatabaseAdministrator {





    public static ArrayList<MiflasonNameObj> miflasNameList;

    public static DatabaseReference myFirebaseRef = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference miflasNameListRef = FirebaseDatabase.getInstance().getReference().child(share.fair.miflas.Constants.MIFLAS_NAME_LIST);

    public static String OriSharabi ="Ori Sharabi";
    public static String AveraTzagay="Avera Tzagay";
    public static String EliavShames ="Eliav Shames";
    public static String EliyaDahan = "Eliya Dahan";
    public static String DavidFreud= "David Freud";
    public static String AviKorzec = "Avi Korzec";
    public static String DanaYona ="Dana Yona";
    public static String TomerCohen ="Tomer Cohen";
    public static String AlexPinkas= "Alex Pinkas";
    public static String YanivAlter ="Yaniv Alter";
    public static String NairaMusilian ="Naira Musilian";
    public static String EhudKlein= "Ehud Klein";
    public static String LitalDavid ="Lital David";
    public static String NevoFishbine ="Nevo Fishbine";
    public static String TzlilAvraham= "Tzlil Avraham";
    public static String YacovPariente = "Yacov Pariente";
    public static String BaruchMekonen ="Baruch Mekonen";
    public static String IgorLapshun ="Igor Lapshun";
    public static String YaaraZohar ="Yaara Zohar";
    public static String YoniAsulin ="Yoni Asulin";

    /**
     *
     */
    public static void loadNameStringsToList(){
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add(OriSharabi);
        stringList.add(AveraTzagay);
        stringList.add(EhudKlein);
        stringList.add(EliavShames);
        stringList.add(EliyaDahan);
        stringList.add(DavidFreud);
        stringList.add(AviKorzec);
        stringList.add(DanaYona);
        stringList.add(TomerCohen);
        stringList.add(AlexPinkas);
        stringList.add(YaaraZohar);
        stringList.add(YanivAlter);
        stringList.add(NairaMusilian);
        stringList.add(LitalDavid);
        stringList.add(NevoFishbine);
        stringList.add(TzlilAvraham);
        stringList.add(YacovPariente);
        stringList.add(BaruchMekonen);
        stringList.add(IgorLapshun);
        stringList.add(YoniAsulin);

        resetMiflasNameList(stringList);
    }
    /**
     * resests THE STATIC LIST
     * @param list
     */
    private static void resetMiflasNameList(ArrayList<String> list){
        miflasNameList = new ArrayList<>();
        for(String name: list ){
            miflasNameList.add(new MiflasonNameObj(name));
            addNamesToList(name);
        }
    }

    /**
     * adds the names to the miflas name list in FIREBASE
     * @param nameListToAdd
     */
    private static void addNamesToList(ArrayList<String> nameListToAdd ){
        for(String name: nameListToAdd){
            MiflasonNameObj miflasonName = new MiflasonNameObj(name);
            miflasNameListRef.push().setValue(miflasonName);
        }
    }
    private static void addNamesToList(String nameToAdd ){
            MiflasonNameObj miflasonName = new MiflasonNameObj(nameToAdd);
            miflasNameListRef.push().setValue(miflasonName);
    }

}
