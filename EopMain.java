import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

public class EopMain {
    public static void main(String[] args) {        
        Scanner scanner = new Scanner(System.in);
        //using ASCII code to design the terminal so it will be colorful and easy to look and read
        final String COLOR[] = {"\u001B[30m","\u001B[31m","\u001B[32m","\u001B[33m","\u001B[34m","\u001B[35m","\u001B[36m","\u001B[37m"};
        /*
        0=\u001B[30m	Black text
        1=\u001B[31m	Red text
        2=\u001B[32m	Green text
        3=\u001B[33m	Yellow text
        4=\u001B[34m	Blue text
        5=\u001B[35m	Magenta text
        6=\u001B[36m	Cyan text
        7=\u001B[37m	White text
         */
        String RESET ="\u001B[0m";
        char restart ='A';
        do{
            restart = 'A';//reset restart to A so it dont keep looping after user choose Y

            System.out.printf("-------WELCOME TO ZAHRUWI TURN BASE GAME-------\n\n");
            System.out.println("Choose text color [0 for BLACK] & [1 for WHITE]");
            System.out.println("Notes:");
            System.out.println("If your terminal background color is white,choose 0.");
            System.out.println("If your terminal background color is black,choose 1.");
            System.out.print("Choice: ");
            do{
                try{
                    int bg;
                    do{
                        bg = scanner.nextInt();
                        scanner.nextLine();
                        if(bg == 0){
                            System.out.print(RESET);
                        }
                        else if(bg == 1){
                            System.out.print(COLOR[0]);
                            RESET = COLOR[0];
                        }
                        if(bg < 0 || bg > 1)
                            System.out.println("Please input a valid number");
                    }while(bg < 0 || bg > 1);
                    break;
                }
                catch (InputMismatchException ex) {
                    System.out.println("Please input a valid number");
                    scanner.nextLine();//Clear scanner buffer after wrong input
                }
            }while(true);

            /*method:
            main
            playTurn
            getPlayerName
            getPlayerAttack
            getPlayerAbility
            getAttackName
            displayWinner
            displayStatus
            displayAttack
            calculateDamage*/
            
            String[][] attacks = {
                {"Punch", "4", "0"},
                {"Kick ", "6", "15"},
                {"Smackdown", "7", "30"},
                {"Ultimate", "25", "40"}
            };
            
            String[] enemyNameIndex = {"Pikachu", "Charizard", "Fanny", "Balmond", "Shiroi", "Kratos","Batman"};
            
            playTurn(scanner, attacks, enemyNameIndex, RESET, COLOR); //call
            System.out.println("\nDo you want to continue (Y/N): ");

            do {
                try {
                    String userInput = scanner.nextLine();
                    if (!userInput.isEmpty()) {
                        restart = userInput.charAt(0);
                    }
                    else {
                        continue;// Go back to the beginning of the loop
                    }  
                    if (restart != 'Y' && restart != 'N' && restart != 'A' /* additional A because i  initialized restart with A so this println will not occur*/) {
                        System.out.println("Please input valid char (Y/N):");
                    }
                    break;
                } catch (InputMismatchException ex) {
                    System.out.println("Please input valid char (Y/N)");
                }
            } while (restart != 'Y' && restart != 'N');
            
            if(restart == 'N')
            break;

        }while(restart != 'N');
        
        scanner.close();
    }

    private static void playTurn(Scanner scanner, String[][] attacks, String[] enemyNameIndex, String RESET, String COLOR[]) {
        Random random = new Random();
        String playerName = getPlayerName(scanner); //call
        int playerHP = 100;
        int playerEnergy = 10;

        int randomIndex = random.nextInt(enemyNameIndex.length);
        String enemyName =enemyNameIndex[randomIndex]; //changed enemyName type to normal string instead of array string because it will conflict with playerName when used in menthod if not changed
        System.out.println("Your enemy is " + enemyName);
        int enemyHP = 100;
        int enemyEnergy = 10;

        boolean[][] abilityLimit = { 
        {true,true,true,true,true},
        {true,true,true,true,true}};

        int[][] limitCount = {{5,3,3,4,3},{5,3,3,4,3}};
        
       

        while (playerHP > 0 && enemyHP > 0) {

            displayStatus(playerName, playerHP, playerEnergy, enemyName, enemyHP, enemyEnergy, randomIndex, RESET, COLOR); //call
            
            int playerPoisonCount = 0;
            boolean playerIsParalyzed = false;
            int enemyPoisonCount = 0;
            boolean enemyIsParalyzed = false;

            int playerAttack = getPlayerAttack(scanner, attacks, playerEnergy, COLOR, RESET); //call
            int playerAbility = getPlayerAbility(scanner,abilityLimit,limitCount,COLOR,RESET);
            int playerDamage = calculateDamage(playerAttack, attacks); //call
            int playerEnergyCost = Integer.parseInt(attacks[playerAttack - 1][2]);
            
            int enemyAttack = getEnemyAttack(random,attacks,enemyEnergy); // Random attack for the enemy
            int enemyDamage = calculateDamage(enemyAttack, attacks); //call
            int enemyEnergyCost = Integer.parseInt(attacks[enemyAttack-1][2]);
            int enemyAbility = getEnemyAbility(abilityLimit, limitCount);
            
            int playerMaxHP = 100;
            int healingAmount = 10;

            if (!playerIsParalyzed) {
                System.out.println("|----------------------------------------------------------------");
                System.out.print("|"+COLOR[2]);
                switch (playerAbility) {
                    case 1:
                        playerDamage = ability1(playerDamage,playerName);
                        limitCount[0][0]-=1;
                        break;
                    case 2:
                        enemyIsParalyzed = ability2(enemyIsParalyzed, playerName, enemyName); 
                        limitCount[0][1]-=1;
                        break;
                    case 3:
                        enemyPoisonCount = ability3(enemyPoisonCount, playerName, enemyName);
                        limitCount[0][2]-=1;
                        break;
                    case 4:
                        enemyDamage = ability4(enemyDamage, playerName, enemyName);
                        limitCount[0][3]-=1;
                        break;
                    case 5:
                        playerHP = ability5(playerHP, playerMaxHP, healingAmount, playerName, COLOR, RESET);
                        limitCount[0][4]-=1;
                        break;
                    default:
                        System.out.println(playerName + " skipped ability!");
                }
                System.out.print(RESET);
            } 
            else {
                System.out.println("|----------------------------------------------------------------");
                System.out.println("|"+COLOR[2]+playerName + " is paralyzed and unable to attack!"+RESET);
            }
            if (!enemyIsParalyzed) {
                System.out.println("|----------------------------------------------------------------");
                System.out.print("|"+COLOR[1]);
                switch (enemyAbility) {
                    case 1:
                        enemyDamage = ability1(enemyDamage,enemyName);
                        limitCount[1][0]-=1;
                        break;
                    case 2:
                        playerIsParalyzed = ability2(playerIsParalyzed, enemyName, playerName);
                        limitCount[1][1]-=1;
                        break;
                    case 3:
                        playerPoisonCount = ability3(playerPoisonCount, enemyName, playerName);
                        limitCount[1][2]-=1;
                        break;
                    case 4:
                        playerDamage = ability4(playerDamage, enemyName, playerName);
                        limitCount[1][3]-=1;
                        break;
                    case 5:
                        enemyHP = ability5(enemyHP, playerMaxHP, healingAmount, enemyName, COLOR, RESET);
                        limitCount[1][4]-=1;
                        break;
                    default:
                        System.out.println(enemyName + " skipped ability!");
                }
                System.out.print(RESET);
            } 
            else {
                System.out.println("|----------------------------------------------------------------");
                System.out.println("|"+COLOR[1]+enemyName + " is paralyzed and unable to attack!"+RESET);

            }
            for(int i=0;i<abilityLimit.length;i++){
                for(int j=0;j<abilityLimit[0].length;j++){
                    if(limitCount[i][j] == 0)
                        abilityLimit[i][j]=false;
                }
            }
            if(!playerIsParalyzed){
                if (enemyPoisonCount > 0) {
                    enemyHP -= 5;
                    enemyPoisonCount--;
                }
            }
            if(!enemyIsParalyzed){
                if (playerPoisonCount > 0) {
                    playerHP -= 5;
                    playerPoisonCount--;
                }
            }
            if(!playerIsParalyzed){
                enemyHP -= playerDamage;
                playerEnergy -= playerEnergyCost; // Deduct mana
                playerEnergy += 7; // Gain mana after basic attack
            }    
            if(!enemyIsParalyzed){
                playerHP -= enemyDamage;
                enemyEnergy -= enemyEnergyCost; // Deduct mana 
                enemyEnergy += 7; // Gain mana after basic attack
            }

            displayAttack(playerName, enemyName, attacks, playerAttack, playerDamage, enemyAttack, enemyDamage, playerIsParalyzed, enemyIsParalyzed, randomIndex, COLOR, RESET); //call
            
            playerIsParalyzed = false;
            enemyIsParalyzed = false;
        }
        displayWinner(playerName, playerHP, enemyName, randomIndex, RESET, COLOR);
    }


    private static String getPlayerName(Scanner scanner) {
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    private static void displayStatus(String playerName, int playerHP, int playerEnergy, String enemyName, int enemyHP, int enemyEnergy, int randomIndex, String RESET, String COLOR[]) {
        System.out.println("\n|| " + COLOR[1] + playerName +RESET+"   \t||"+ COLOR[2] +" HP: " + playerHP +RESET+ " || "+COLOR[4]+"Energy: "+ playerEnergy+RESET+" ||");
        System.out.println("|| "+ COLOR[1] +enemyName +RESET+"\t||"+ COLOR[2] +" HP: "+ enemyHP +RESET+ " || "+COLOR[4]+"Energy: " + enemyEnergy+RESET+" ||");
    }
    
    private static int getPlayerAttack(Scanner scanner, String[][] attacks, int playerEnergy, String[] COLOR, String RESET) {
        int attack;
        do{
            try{
                System.out.println("\nAttack list:");
                for (int i = 0; i < attacks.length; i++) {
                    System.out.println((i + 1) + ". "+COLOR[3]+ attacks[i][0] +RESET+ "\t||" +RESET+COLOR[1]+" Dmg: "+ attacks[i][1] +RESET+ " ||" +RESET+COLOR[4]+" Energy: " + attacks[i][2] +RESET+ " ||");
                }
                System.out.print("Select attack: ");
                attack = scanner.nextInt();//test
               
                if (attack >= 1 && attack <= attacks.length && playerEnergy >= Integer.parseInt(attacks[attack - 1][2])) {
                    break;
                } 
                else {
                    System.out.println("Invalid selection or insufficient energy. Please choose a valid attack (1-" + attacks.length + ").");
                }
            }
            catch (InputMismatchException ex) {
                System.out.println("Please input a valid number.");
                // Clear the scanner buffer
                scanner.nextLine();
            }
        } while (true);

        return attack;
    }

    private static int getEnemyAttack (Random random,String attacks [][], int enemyEnergy) {
        int attack=0;
        for(int i=3;i>=0;i--){
            if(enemyEnergy >= Integer.parseInt(attacks[i][2])){
                attack = random.nextInt(i+1);
                break; 
            }
        }
        return attack+1;
    }

    private static int calculateDamage(int attack, String[][] attacks) {
        int attackx =Integer.parseInt(attacks[attack - 1][1]);
        switch (attack) {
            case 1:
                return attackx;
            case 2:
                return attackx;
            case 3:
                return attackx;
            case 4:
                return attackx;
            default:
                return 0;
        }
    }

    private static int getPlayerAbility(Scanner scanner, boolean[][] abilityLimit, int[][] count, String[] COLOR, String RESET) {
        int ability;
        System.out.printf("\nAbility list:\n");
        System.out.printf("0.%sSkip abilitiy%s\n",COLOR[3],RESET);
        System.out.printf("1.%sBoost %s(Dmg: Multiply 1.5x) %s\t\t      ||%s Limit: %d%s\n", COLOR[3], COLOR[1],RESET, COLOR[6], count[0][0], RESET);
        System.out.printf("2.%sParalyze enemy %s(Enemy turn will be skipped) %s||%s Limit: %d%s\n", COLOR[3], COLOR[1],RESET, COLOR[6], count[0][1], RESET);
        System.out.printf("3.%sPoison %s(Enemy health drops 5 for 4 turns) %s  ||%s Limit: %d%s\n", COLOR[3], COLOR[1],RESET, COLOR[6], count[0][2], RESET);
        System.out.printf("4.%sDefense %s(Enemy attack multiplied by 0.7) %s   ||%s Limit: %d%s\n", COLOR[3], COLOR[1],RESET, COLOR[6], count[0][3], RESET);
        System.out.printf("5.%sHeal %s(Health +10) %s\t\t\t      || %sLimit: %d%s\n", COLOR[3], COLOR[1],RESET, COLOR[6], count[0][4], RESET);
        System.out.print("Select your ability: ");
        do {
            try{
                do{
                    ability = scanner.nextInt();
                    System.out.println();
                    if (ability == 0)
                        break;
                    else if (ability < 1 || ability > 5) {
                        System.out.println(COLOR[1]+"Invalid selection."+RESET+" Pleagse choose a valid ability (1-5).");
                    }
                    for (int i = 0; i < abilityLimit[0].length; i++) {
                        if (!abilityLimit[0][i] && (ability - 1) == i) {
                            System.out.println("You have reached the limit on this ability. Choose another one.");
                            ability = 0;  
                            break;
                        }
                    }
                }while(ability < 1 || ability > 5);
                break;
            }
            catch(InputMismatchException ex) {
                System.out.println(COLOR[1]+"Invalid input."+RESET+"Please input a valid number.");
                scanner.nextLine();
            }

        } while (true);

        return ability;
    }

    private static int getEnemyAbility(boolean[][] abilityLimit, int[][] count) {
        int ability;
        do {
            ability = (int) (Math.random() * 5+1) + 0; 
            if(ability == 0)
                break;
            for (int i = 0; i < abilityLimit[1].length; i++) {
                if (!abilityLimit[1][i] && (ability - 1) == i) {
                    ability=0;
                    break;
                }
            }
        } while (ability < 1 || ability > 5);
    
        return ability;
    }

    private static void displayAttack(String playerName, String enemyName,String[][] attacks, int playerAttack, int playerDamage, int enemyAttack, int enemyDamage, boolean playerIsParalyzed, boolean enemyIsParalyzed, int randomIndex, String[] COLOR, String RESET) {
        String playerAttackName = attacks[playerAttack-1][0];
        String enemyAttackName = attacks[enemyAttack-1][0];
        if (!playerIsParalyzed) {
            System.out.println("|----------------------------------------------------------------");
            //System.out.println("|" +COLOR[2] +playerName +RESET+ " used "+COLOR[1] + playerAttackName +RESET+ " and dealt "+COLOR[1] + playerDamage +RESET+" damage to "+COLOR[2] + enemyName + RESET);
            System.out.println("|" +COLOR[2] +playerName+ " used "+playerAttackName+ " and dealt "+ playerDamage +" damage to "+ enemyName + RESET);
        } 
        if (playerIsParalyzed) {
            System.out.println("|----------------------------------------------------------------");
            System.out.println("|"+COLOR[2] + playerName + " is paralyzed and unable to attack!"+RESET);
        }
        if (!enemyIsParalyzed) {
            System.out.println("|----------------------------------------------------------------");
            System.out.println("|"+COLOR[1] + enemyName+ " used " + enemyAttackName + " and dealt " + enemyDamage + " damage to " + playerName + RESET);
        }
        if (enemyIsParalyzed) {
            System.out.println("|----------------------------------------------------------------");
            System.out.println("|"+COLOR[1] + enemyName + " is paralyzed and unable to attack!"+RESET);
        }
        System.out.println("|----------------------------------------------------------------");
    }


    private static int ability1 (int damage,String name){
        damage = (int) (damage* 1.25); // Basic attack gets bonus damage
        System.out.println(name + " used Boost! Attack power increased by 25%!");
        return damage;
    }

    private static boolean ability2 (boolean isParalyzed, String attackerName, String targetName){
        isParalyzed = true;
        System.out.println(attackerName+ " used Paralyze! " + targetName + " will be unable to attack next turn!");
        return isParalyzed;
    }

    private static int ability3 (int poisonCount, String attckerName, String targetName){
        poisonCount = 4;
        System.out.println(attckerName + " used Poison! " + targetName + " will lose 5 HP for 4 turns!");
        return poisonCount;
    }

    private static int ability4 (int damage, String attackerName, String targetName){
        damage = (int) (damage * 0.7);
        System.out.println(attackerName + " used Smokescreen! " + targetName + "'s attack power is weakened by 30%!");
        return damage;
    }

    private static int ability5 (int hp, int playerMaxHP, int healingAmount, String name, String[] COLOR, String RESET){
        if (hp < playerMaxHP) {
            if (hp + healingAmount > playerMaxHP) {
                hp = playerMaxHP;
            } 
            else {
                hp+= healingAmount;
            }
            System.out.println(name + " used Heal! " + name + " recovers "+COLOR[2] + healingAmount + " HP!"+RESET);
        } else {
            System.out.println(name+ " is already at full health!");
        }
        return hp;
    }
    
    private static void displayWinner(String playerName, int playerHP, String enemyName,int randomIndex,String RESET,String COLOR[] ) {
        if (playerHP <= 0) {
            System.out.println("\n"+COLOR[1] + playerName + " is defeated. " +COLOR[2]+ enemyName + " is the winner!");
        } else {
            System.out.println("\n"+COLOR[1]+ enemyName + " is defeated. "+COLOR[2] + playerName + " is the winner!");
        }
        System.out.print(RESET);
    }
}

            
 
