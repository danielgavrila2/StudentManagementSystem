import db_objs.User;
import gui.*;

import javax.swing.*;

public class AppLaucher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGui().setVisible(true);

                //new FormularRegister(new User(2, "Denisa", "deni2004", "STUDENT")).setVisible(true);

                //new RegisterGui().setVisible(true);

//                new MainMenuGui(
//                        new User(4, "Daniel", "1234", "ADMIN")
//                ).setVisible(true);

//                new ManageProfileGui(new User(4, "Daniel", "1234", "ADMIN")).setVisible(true);

                // new SchoolSituationGui(new User(2, "Denisa", "deni2004", "STUDENT")).setVisible(true);

             //   new ViewCoursesGui(new User(2, "Denisa", "deni2004", "STUDENT")).setVisible(true);

               // new ElectronicCatalogGui(new User(4, "Daniel", "1234", "ADMIN")).setVisible(true);

             //   new ManageUsersGui(new User(4, "Daniel", "1234", "ADMIN")).setVisible(true);
            }
        });
    }
}
