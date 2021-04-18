package net.dajman.rentalcar.ui.utils;


import javafx.scene.image.Image;
import net.dajman.rentalcar.App;

public class Images {

    public static final transient Image btnClose = new Image(App.class.getResourceAsStream("ui/img/btn_close.png"));
    public static final transient Image btnCloseHover = new Image(App.class.getResourceAsStream("ui/img/btn_close_t.png"));
    public static final transient Image btnMinimalize = new Image(App.class.getResourceAsStream("ui/img/btn_minimize.png"));
    public static final transient Image btnMinimalizeHover = new Image(App.class.getResourceAsStream("ui/img/btn_minimize_t.png"));
    public static final transient Image btnRestore = new Image(App.class.getResourceAsStream("ui/img/btn_restore.png"));
    public static final transient Image btnRestoreHover = new Image(App.class.getResourceAsStream("ui/img/btn_restore_t.png"));

    public static final transient Image imageEmpty = new Image(App.class.getResourceAsStream("ui/img/no_image.png"));
    public static final transient Image loadingGif = new Image(App.class.getResourceAsStream("ui/img/loading.gif"));
    public static final transient Image imageUser = new Image(App.class.getResourceAsStream("ui/img/user.png"));



}