package com.example.planetgreece;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.Login.LoginTabFragment;
import com.example.planetgreece.fragment.Main.HomeFragment;
import com.example.planetgreece.fragment.Main.ProfileFragment;
import com.example.planetgreece.fragment.Main.SavedFragment;
import com.example.planetgreece.fragment.Main.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper db;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = DatabaseHelper.getInstance(getApplicationContext());

        /**
         * DEBUGGING
         */
        addArticles(); //Add articles
        /**
         * END DEBUGGING
         */

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra(LoginTabFragment.USER_OBJECT);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainConstaint, HomeFragment.newInstance(user)).commit();
        bottomNavigationView.setSelectedItemId(R.id.navHome);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
//                    case R.id.navHome:
//                        fragment = HomeFragment.newInstance(user);
//                        break;
                    case R.id.navSaved:
                        fragment = SavedFragment.newInstance(user);
                        break;
                    case R.id.navProfile:
                        fragment = ProfileFragment.newInstance(user);
                        break;
                    case R.id.navSettings:
                        fragment = SettingsFragment.newInstance(user);
                        break;
                    default:
                        fragment = HomeFragment.newInstance(user);
                        break;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.mainConstaint, fragment)
                        .commit();

                return true;
            }
        });
    }

    private void addArticles() {
        db.createArticle(new Article(
                "Θωμάς ο… Survivor!",
                "https://www.arcturos.gr/files/temp/88E1A30F5464E88732B0F8459DECE63D.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/thomassurvivor/",
                "10-05-2022"));

        db.createArticle(new Article(
                "Συνύπαρξη με σεβασμό στα όρια της άγριας ζωής",
                "https://www.arcturos.gr/files/temp/9F56B0F96F970414D29821401353DA59.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/lykosparnitha/",
                "10-01-2022"));

        db.createArticle(new Article(
                "Ολοκληρώθηκε το πρόγραμμα ενίσχυσης της επιχειρησιακής ετοιμότητας του ΑΡΚΤΟΥΡΟΥ στην περίθαλψη και επανένταξη μεγάλων ειδών της Άγριας Πανίδας",
                "https://www.arcturos.gr/files/temp/CE5B24AB2E74DB402E8BAA5C51A75359.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/prasinotameio/",
                "16-12-2021"));

        db.createArticle(new Article(
                "Ανεξέλεγκτη η δηλητηρίαση της άγριας ζωής: κατεπείγον αίτημα δράσης ενάντια στα δηλητηριασμένα δολώματα",
                "https://www.arcturos.gr/files/temp/5058C5EB364BD87C27B359CE127D78EC.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/drasigiatadolwmata/",
                "04-11-2021"));

        db.createArticle(new Article(
                "Άμεση συνεργασία φορέων για την παρουσία αρκούδας στην περιοχή του ποταμού Αξιού και επόμενες ενέργειες",
                "https://www.arcturos.gr/files/temp/9F1100B410C0CC80DB42AD749C5D6FFB.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/arkoudaanatoliko/",
                "25-10-2021"));

        db.createArticle(new Article(
                "Λύγκας: Ο άγνωστος «τίγρης» της Ευρώπης στο Κέντρο Προστασίας του ΑΡΚΤΟΥΡΟΥ\n",
                "https://www.arcturos.gr/files/temp/DD4EB9C60C2DA421EB2DC6F6975BE6CC.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/lynx/",
                "23-09-2021"));

        db.createArticle(new Article(
                "Νεκρή αρκούδα από πυροβολισμό στην Πρέσπα",
                "https://www.arcturos.gr/files/temp/EB6AA07A148058311657EB3B0AB6251F.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/nekriarkoudaprespa/",
                "16-09-2021"));

        db.createArticle(new Article(
                "Ο ΑΡΚΤΟΥΡΟΣ δίνει μία δεύτερη ευκαιρία στη φύση σε έξι ορφανά άγρια ζώα",
                "https://www.arcturos.gr/files/temp/980FD49169D7665BA8F4822D6FC82011.jpg",
                "Αρκτούρος",
                "https://www.arcturos.gr/news/exiorfanaagriazwa/",
                "29-07-2021"));

        db.createArticle(new Article(
                "Ακόμα ένα περιστατικό δηλητηριασμένων δολωμάτων στον Ίασμο",
                "https://wwfeu.awsassets.panda.org/img/original/el_1_1.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6633841",
                "19-05-2022"));

        db.createArticle(new Article(
                "Ευρωπαϊκή Ημέρα Natura 2000: Μια γιορτή για την προστασία της φύσης",
                "https://wwfeu.awsassets.panda.org/img/original/natura_2_prl.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6633366",
                "19-05-2022"));

        db.createArticle(new Article(
                "Νέο στοίχημα για τη Θαλάσσια Προστατευόμενη Περιοχή της Γυάρου",
                "https://wwfeu.awsassets.panda.org/img/original/gyaros2.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6603916",
                "16-05-2022"));

        db.createArticle(new Article(
                "Ενεργειακή αποδοτικότητα: Μια παραμελημένη λύση για την ενεργειακή κρίση",
                "https://wwfeu.awsassets.panda.org/img/original/energopoiisou_odigos.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6603441",
                "16-05-2022"));

        db.createArticle(new Article(
                "Τα οικονομικά της πρόληψης και της καταστολής των δασικών πυρκαγιών για πρώτη φορά σε μια αναλυτική έκθεση από το WWF",
                "https://wwfeu.awsassets.panda.org/img/original/prolipsi_prl_1.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6574466",
                "12-05-2022"));

        db.createArticle(new Article(
                "«Υιοθέτησε μια παραλία»: Πρώτος χρόνος υλοποίησης",
                "https://wwfeu.awsassets.panda.org/img/adopt_prl_767281.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6490866",
                "27-04-2022"));

        db.createArticle(new Article(
                "Να μη γίνει η ενεργειακή κρίση αφορμή για ξεπερασμένες και επικίνδυνες πολιτικές",
                "https://wwfeu.awsassets.panda.org/img/original/energeiaki_krisi.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6422941",
                "18-04-2022"));

        db.createArticle(new Article(
                "Πρόταση του WWF Ελλάς για ένα συμμετοχικό σύστημα διαχείρισης της αλιείας",
                "https://wwfeu.awsassets.panda.org/img/original/fishing_boat_1.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6403941",
                "13-04-2022"));

        db.createArticle(new Article(
                "Επιτακτική ανάγκη για ένα καλύτερο μοντέλο ανάπτυξης των ΑΠΕ",
                "https://wwfeu.awsassets.panda.org/img/original/wind_turbines_invite.jpg",
                "WWF",
                "https://www.wwf.gr/ta_nea_mas/?uNewsID=6358816",
                "05-04-2022"));

        db.createArticle(new Article(
                "FREE SOLAR: Μειώνουμε τον λογαριασμό ρεύματος για πάντα",
                "https://www.greenpeace.org/static/planet4-greece-stateless/2022/05/c8448462-free-solar1.png",
                "Greenpeace",
                "https://www.greenpeace.org/greece/issues/klima/46643/free-solar-reyma-hliaki-energeia-thessalia-2022/",
                "10-05-2022"));

        db.createArticle(new Article(
                "Νέα έρευνα: οι πολίτες θέλουν εξοικονόμηση ενέργειας και ΑΠΕ για να βγούμε από την ενεργειακή κρίση!",
                "https://www.greenpeace.org/static/planet4-greece-stateless/2014/11/GP0STR5KN_PressMedia-1024x755.jpg",
                "Greenpeace",
                "https://www.greenpeace.org/greece/issues/klima/46555/nea-ereyna-greenpeace-ananevsimes-piges-energeia-ellada/",
                "07-04-2022"));

        db.createArticle(new Article(
                "Η Ευρώπη θα “ψωνίσει” ενέργεια από διαφορετικούς “δεσπότες”",
                "https://www.greenpeace.org/static/planet4-greece-stateless/2022/03/c932a6dc-gp1sx0ac-1024x683.jpg",
                "Greenpeace",
                "https://www.greenpeace.org/greece/issues/klima/46500/eyropi-poutin-polemos-energeia-orykta-kaysima/",
                "28-03-2022"));

        db.createArticle(new Article(
                "Οι Γύπες στα Ακαρνανικά Όρη: ικανοποίηση για το παρόν, τεράστια ανησυχία για το μέλλον",
                "https://www.ornithologiki.gr/images/nea/header_banner_Akarnanika.jpg",
                "Ορνιθολογική",
                "https://www.ornithologiki.gr/el/enhmerwsh-ekpaideush/enimerosi/ta-nea-mas/1481-oi-gypes-sta-akarnanika-ori-ikanopoiisi-gia-to-paron-terastia-anisyxia-gia-to-mellon",
                "18-05-2022"));

        db.createArticle(new Article(
                "Καταγράφοντας την ανοιξιάτικη μετανάστευση: αποτελέσματα των Αγώνων Παρατήρησης Πουλιών 2022",
                "https://www.ornithologiki.gr/images/banners/header_banner_birdwatching.jpg",
                "Ορνιθολογική",
                "https://www.ornithologiki.gr/el/enhmerwsh-ekpaideush/enimerosi/ta-nea-mas/1475-katagrafontas-tin-anoiksiatiki-metanastefsi-apotelesmata-ton-agonon-paratirisis-poulion-2022",
                "15-05-2022"));

        db.createArticle(new Article(
                "Μικρά νησιά της Μεσογείου: σταθμοί ζωής για τα μεταναστευτικά πουλιά",
                "https://www.ornithologiki.gr/images/banners/header_banner_flock.jpg",
                "Ορνιθολογική",
                "https://www.ornithologiki.gr/el/enhmerwsh-ekpaideush/enimerosi/ta-nea-mas/1477-mikra-nisia-tis-mesogeiou-stathmoi-zois-gia-ta-metanasteftika-poulia",
                "12-05-2022"));

        db.createArticle(new Article(
                "Η παράνομη δηλητηρίαση της άγριας ζωής αφανίζει τη βιοποικιλότητα των Βαλκανίων",
                "https://www.ornithologiki.gr/images/banners/header_banner_poisoning.jpg",
                "Ορνιθολογική",
                "https://www.ornithologiki.gr/el/enhmerwsh-ekpaideush/enimerosi/ta-nea-mas/1471-i-paranomi-dilitiriasi-tis-agrias-zois-afanizei-ti-viopoikilotita-ton-valkanion",
                "28-04-2022"));

        db.createArticle(new Article(
                "\"Ρεκόρ\" Όρνιων με πομπό και προορισμό... την Αιτωλοακαρνανία",
                "https://www.ornithologiki.gr/images/banners/Gyps_fulvus_Crete_Panos__Perantonakis_header.jpg",
                "Ορνιθολογική",
                "https://www.ornithologiki.gr/el/enhmerwsh-ekpaideush/enimerosi/ta-nea-mas/1470-rekor-orneon-me-pompo-kai-proorismo-tin-aitoloakarnania",
                "27-04-2022"));

        db.createArticle(new Article(
                "Δράσεις καθαρισμού του βυθού στη Νησίδα Πατρόκλου",
                "https://www.medasset.org/wp-content/uploads/2022/05/%CE%BA%CE%B1%CE%B8%CE%B1%CF%81%CE%B9%CF%83%CE%BC%CE%BF%CE%B9-%CE%B2%CF%85%CE%B8%CE%BF%CF%85.jpg",
                "MEDASSET",
                "https://www.medasset.org/el/katharismoi-vithou-nisida-patroklou/",
                "17-05-2022"));

        db.createArticle(new Article(
                "Διαδικτυακή Διημερίδα: Διατήρηση των “Σκοτεινών Ουρανών” για την Αειφορία, τις Aστρονομικές Παρατηρήσεις, την Yγεία, τα Oικοσυστήματα και τονΠολιτισμό",
                "https://www.medasset.org/wp-content/uploads/2022/05/%CE%A6%CF%89%CF%84%CE%BF%CF%81%CF%8D%CF%80%CE%B1%CE%BD%CF%83%CE%B7-%CE%94%CE%B9%CE%B7%CE%BC%CE%B5%CF%81%CE%AF%CE%B4%CE%B1.jpg",
                "MEDASSET",
                "https://www.medasset.org/el/diadiktyaki-diimerida-diatirisi-ton-skoteinon-ouranon/",
                "06-05-2022"));

        db.createArticle(new Article(
                "Να σταματήσει η καταστροφή των αστικών δέντρων!",
                "https://www.medasset.org/wp-content/uploads/2022/04/%CE%9A%CE%BB%CE%AC%CE%B4%CE%B5%CE%BC%CE%B1-%CE%B4%CE%AD%CE%BD%CF%84%CF%81%CF%89%CE%BD-%CE%91%CE%BD%CE%B1%CE%BA%CE%BF%CE%AF%CE%BD%CF%89%CF%83%CE%B7.jpg",
                "MEDASSET",
                "https://www.medasset.org/el/na-stamatisei-i-katastrofi-ton-astikon-dentron/",
                "14-04-2022"));

        db.createArticle(new Article(
                "Κοινό Δ.Τ.: Ανάπτυξη … εκτός ορίων, ακόμα και σε πυρήνες Εθνικών Πάρκων;",
                "https://www.eepf.gr/images/fysi/papingo1.jpg",
                "Ε.Ε.Π.Φ.",
                "https://www.eepf.gr/el/el-nea/papingo-oria",
                "19-05-2022"));

        db.createArticle(new Article(
                "Σεμινάριο με θέμα «Αρχαίες Πόλεις και Γηγενής Χλωρίδα»",
                "https://www.eepf.gr/images/EUProjects/2new3aa17e6bd6311fd942358dadbb9a795a-V.jpg",
                "Ε.Ε.Π.Φ.",
                "https://www.eepf.gr/el/el-nea/%CF%83%CE%B5%CE%BC%CE%B9%CE%BD%CE%AC%CF%81%CE%B9%CE%BF-%CE%BC%CE%B5-%CE%B8%CE%AD%CE%BC%CE%B1-%C2%AB%CE%B1%CF%81%CF%87%CE%B1%CE%AF%CE%B5%CF%82-%CF%80%CF%8C%CE%BB%CE%B5%CE%B9%CF%82-%CE%BA%CE%B1%CE%B9-%CE%B3%CE%B7%CE%B3%CE%B5%CE%BD%CE%AE%CF%82-%CF%87%CE%BB%CF%89%CF%81%CE%AF%CE%B4%CE%B1%C2%BB",
                "18-05-2022"));

        db.createArticle(new Article(
                "Άμεση διακοπή των σεισμικών ερευνών στο Ιόνιο ζητούν από την κυβέρνηση 15 περιβαλλοντικές οργανώσεις, μετά από τρία περιστατικά εκθαλάσσωσης ζιφιών στην περιοχή",
                "https://www.callisto.gr/sites/www.callisto.gr/files/styles/product_teaser/public/blog/images/ziphios_arion.jpg?itok=wyiUyU7n",
                "Καλλιστώ",
                "https://www.callisto.gr/blog/amesi-diakopi-ton-seismikon-ereynon-sto-ionio-zitoyn-apo-tin-kyvernisi-15-perivallontikes",
                "19-05-2022"));

        db.createArticle(new Article(
                "Η προστασία της άγριας ζωής και των ελληνικών θαλασσών στο προσκήνιο",
                "https://www.callisto.gr/sites/www.callisto.gr/files/styles/product_teaser/public/blog/images/ziphios_dead.jpg?itok=M7e_gpzx",
                "Καλλιστώ",
                "https://www.callisto.gr/blog/i-prostasia-tis-agrias-zois-kai-ton-ellinikon-thalasson-sto-proskinio",
                "08-02-2022"));

        db.createArticle(new Article(
                "Ενημέρωση για το ευρύ κοινό με αφορμή την πιθανή επίθεση λύκου σε σκύλους στον Διόνυσο",
                "https://www.callisto.gr/sites/www.callisto.gr/files/styles/product_teaser/public/blog/images/parnitha_wolf_pup3.jpg?itok=BsKeAEU3",
                "Καλλιστώ",
                "https://www.callisto.gr/blog/enimerosi-gia-eyry-koino-me-aformi-tin-pithani-epithesi-lykoy-se-skyloys-ston-dionyso",
                "02-02-2022"));

        db.createArticle(new Article(
                "Εργαστήρι για την αντιμετώπιση περιστατικών πετρελαιωμένων θαλάσσιων χελωνών",
                "https://www.archelon.gr/contents/photos/Image/articles/photos_2022/OILSPIL2.jpg",
                "ΑΡΧΕΛΩΝ",
                "https://www.archelon.gr/contents/ournews.php?mid=6&nid=1200",
                "06-05-2022"));

        db.createArticle(new Article(
                "Ένα από τα καλύτερα προγράμματα για την προστασία της φύσης στην Ευρώπη!",
                "https://www.archelon.gr/contents/photos/Image/articles/photos_2022/citizenaward.jpg",
                "ΑΡΧΕΛΩΝ",
                "https://www.archelon.gr/contents/ournews.php?mid=6&nid=1194",
                "05-04-2022"));

        db.createArticle(new Article(
                "Η ωοτοκία των θαλάσσιων χελωνών στη νότια Πελοπόννησο",
                "https://www.archelon.gr/contents/photos/Image/articles/photos_2022/ARCHELON_LAKONIKOS_1.jpg",
                "ΑΡΧΕΛΩΝ",
                "https://www.archelon.gr/contents/ournews.php?mid=6&nid=1186",
                "27-01-2022"));

    }
}