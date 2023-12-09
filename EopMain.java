import java.util.Random;
import java.util.Scanner;

public class EopMain {
    public static void main(String[] args) {
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
        final String RESET ="\u001B[0m";
        System.out.print(RESET);//Normalize terminal color white for all user (vscode/netbeans/etc)

        //method:
        //main
        //playTurn
        //getPlayerName
        //getPlayerAttack
        //getPlayerAbility
        //getAttackName
        //displayWinner
        //displayStatus
        //displayAttack
        //calculateDamage
        
        Scanner input = new Scanner(System.in);
        String[][] attacks = {
            {"Punch", "4", "0"},
            {"Kick", "6", "15"},
            {"Smackdown", "7", "30"},
            {"Ultimate", "25", "40"}
        };
        
        String[] enemyName = {
            "Pikachu", "Charizard", "Fanny", "Balmond", "Yuri"};

        playTurn(input, attacks, enemyName, RESET, COLOR); //call
        
        input.close();
    }

    private static void playTurn(Scanner input, String[][] attacks, String[] enemyName,String RESET, String COLOR[]) {
        Random random = new Random();
        String playerName = getPlayerName(input); //call
        int playerHP = 100;
        int playerMana = 10;

        int randomIndex = random.nextInt(enemyName.length);
        System.out.println("Your enemy is " + enemyName[randomIndex]);
        int enemyHP = 100;
        int enemyMana = 10;

        while (playerHP > 0 && enemyHP > 0) {

            displayStatus(playerName, playerHP, playerMana, enemyName, enemyHP, enemyMana, randomIndex, RESET, COLOR); //call
            
            int playerPoisonCount = 0;
            boolean playerIsParalyzed = false;
            int enemyPoisonCount = 0;
            boolean enemyIsParalyzed = false;

            int playerAttack = getPlayerAttack(input, attacks, playerMana); //call
            int playerAbility = getPlayerAbility(input);
            int playerDamage = calculateDamage(playerAttack, attacks); //call
            int playerManaCost = Integer.parseInt(attacks[playerAttack - 1][2]);
            
            
            int enemyAttack = getEnemyAttack(random,attacks,enemyMana); // Random attack for the enemy
            int enemyDamage = calculateDamage(enemyAttack, attacks); //call
            int enemyManaCost = Integer.parseInt(attacks[enemyAttack-1][2]);
            int enemyAbility = random.nextInt(5) + 1;
            

            int playerMaxHP = 100;
            int healingAmount = 10;

            if (!playerIsParalyzed) {
                switch (playerAbility) {
                    case 1:
                        playerDamage = (int) (playerDamage * 1.25); // Basic attack gets bonus damage
                        System.out.println(playerName + " used Boost! Attack power increased by 25%!");
                        break;
                    case 2:
                        enemyIsParalyzed = true;
                        System.out.println(playerName + " used Paralyze! " + enemyName[randomIndex] + " will be unable to attack next turn!");
                        break;
                    case 3:
                        enemyPoisonCount = 4;
                        System.out.println(playerName + " used Poison! " + enemyName[randomIndex] + " will lose 5 HP for 4 turns!");
                        break;
                    case 4:
                        enemyDamage = (int) (playerDamage * 0.7);
                        System.out.println(playerName + " used Defense! " + enemyName[randomIndex] + "'s attack power is weakened by 30%!");
                        break;
                    case 5:
                        if (playerHP < playerMaxHP) {
                            if (playerHP + healingAmount > playerMaxHP) {
                                playerHP = playerMaxHP;
                            } else {
                                playerHP += healingAmount;
                            }
                            System.out.println(playerName + " used Heal! " + playerName + " recovers " + healingAmount + " HP!");
                        } else {
                            System.out.println(playerName + " is already at full health!");
                        }
                        break;
                    default:
                        System.out.println(playerName + " used an invalid ability!");
                }
                
            } 
            else {
                System.out.println(playerName + " is paralyzed and unable to attack!");
            }
            if (!enemyIsParalyzed) {
                switch (enemyAbility) {
                    case 1:
                        enemyDamage = (int) (enemyDamage * 1.25); // Basic attack gets bonus damage
                        System.out.println(enemyName[randomIndex] + " used Boost! Attack power increased by 25%!");
                        break;
                    case 2:
                        playerIsParalyzed = true;
                        System.out.println(enemyName[randomIndex] + " used Paralyze! " + playerName + " will be unable to attack next turn!");
                        break;
                    case 3:
                        playerPoisonCount = 4;
                        System.out.println(enemyName[randomIndex] + " used Poison! " + playerName + " will lose 5 HP for 4 turns!");
                        break;
                    case 4:
                        playerDamage = (int) (enemyDamage * 0.7);
                        System.out.println(enemyName[randomIndex] + " used Smokescreen! " + playerName + "'s attack power is weakened by 30%!");
                        break;
                    case 5:
                        if (enemyHP < playerMaxHP) {
                            if (enemyHP + healingAmount > playerMaxHP) {
                                enemyHP = playerMaxHP;
                            } 
                            else {
                                enemyHP += healingAmount;
                            }
                            System.out.println(enemyName[randomIndex] + " used Heal! " + enemyName[randomIndex] + " recovers " + healingAmount + " HP!");
                        } else {
                            System.out.println(enemyName[randomIndex] + " is already at full health!");
                        }
                        break;
                    default:
                        System.out.println(enemyName[randomIndex] + " used an invalid ability!");
                }
                

            } else {
                System.out.println(enemyName[randomIndex] + " is paralyzed and unable to attack!");
            }
            if (enemyPoisonCount > 0) {
                enemyHP -= 5;
                enemyPoisonCount--;
            }
            if (playerPoisonCount > 0) {
                playerHP -= 5;
                playerPoisonCount--;
            }
            if(!playerIsParalyzed){
                enemyHP -= playerDamage;
                playerMana -= playerManaCost; // Deduct mana
                playerMana += 7; // Gain mana after basic attack
            }    
            if(!enemyIsParalyzed){
                playerHP -= enemyDamage;
                enemyMana -= enemyManaCost; // Deduct mana 
                enemyMana += 7; // Gain mana after basic attack
            }

            playerIsParalyzed = false;
            enemyIsParalyzed = false;

            displayAttack(playerName, enemyName, playerAttack, playerDamage, enemyAttack, enemyDamage, playerIsParalyzed, enemyIsParalyzed, randomIndex); //call

            
        }
        displayWinner(playerName, playerHP, enemyName, randomIndex, RESET, COLOR);
    }

    private static String getPlayerName(Scanner input) {
        System.out.print("Enter your name: ");
        return input.nextLine();
    }

    private static void displayStatus(String playerName, int playerHP, int playerMana, String[] enemyName, int enemyHP, int enemyMana, int randomIndex, String RESET, String COLOR[]) {
        System.out.println("\n|| " + COLOR[1] + playerName +RESET+"\t||"+ COLOR[2] +" HP: " + playerHP +RESET+ " ||\t"+COLOR[4]+" Mana: "+ playerMana+RESET+" ||");
        System.out.println("|| "+ COLOR[1] +enemyName[randomIndex] +RESET+"\t||"+ COLOR[2] +" HP: "+ enemyHP +RESET+ " ||\t"+COLOR[4]+" Mana: " + enemyMana+RESET+" ||");
    }

    private static int getPlayerAttack(Scanner input, String[][] attacks, int playerMana) {
        int attack;
        do {
            System.out.println("\nAttack list:");
            for (int i = 0; i < attacks.length; i++) {
                System.out.println((i + 1) + ". " + attacks[i][0] + " (Dmg: " + attacks[i][1] + ") (Mana: " + attacks[i][2] + ")");
            }
            System.out.print("Select attack: ");
            attack = input.nextInt();

            if (attack < 1 || attack > attacks.length) {
                System.out.println("Invalid selection. Please choose a valid attack (1-" + attacks.length + ").");
            } else if (playerMana < Integer.parseInt(attacks[attack - 1][2])) {
                System.out.println("Insufficient mana! You need " + attacks[attack - 1][2] + " mana to use this attack.");
            }

        } while (attack < 1 || attack > attacks.length || playerMana < Integer.parseInt(attacks[attack-1][2]));
        
        while (attack < 1 || attack > attacks.length || playerMana < Integer.parseInt(attacks[attack - 1][2])) {
            System.out.println("Please choose a valid attack (1-" + attacks.length + ") that you have enough mana for.");
            System.out.print("Select attack: ");
            attack = input.nextInt();
        }

        return attack;
    }

    private static int getEnemyAttack (Random random,String attacks [][], int enemyMana) {
        int attack=0;
        for(int i=3;i>=0;i--){
            if(enemyMana >= Integer.parseInt(attacks[i][2])){
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

    private static int getPlayerAbility(Scanner input) {
        int ability;
        do {
            System.out.println("\nAbility list:");
            System.out.println("1. Boost (Dmg: Multiply 1.5x)");
            System.out.println("2. Paralyze enemy (Enemy turn will be skipped)");
            System.out.println("3. Poison (Enemy health drops 5 for 4 turns)");
            System.out.println("4. Defense (Enemy attack multiplied by 0.7)");
            System.out.println("5. Heal (Health +10)");
            System.out.print("Select your ability: ");
            ability = input.nextInt();
            System.out.println();

            if (ability < 1 || ability > 5) {
                System.out.println("Invalid selection. Please choose a valid ability (1-5).");
            }

        } while (ability < 1 || ability > 5);

        return ability;
    }

    private static void displayAttack(String playerName, String[] enemyName, int playerAttack, int playerDamage, int enemyAttack, int enemyDamage, boolean playerIsParalyzed, boolean enemyIsParalyzed, int randomIndex) {
        String playerAttackName = getAttackName(playerAttack);
        String enemyAttackName = getAttackName(enemyAttack);
        if (!playerIsParalyzed) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|" + playerName + " used " + playerAttackName + " and dealt " + playerDamage + " damage to " + enemyName[randomIndex] + "|");
        } 
        if (playerIsParalyzed) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|" + playerName + " is paralyzed and unable to attack!");
        }
        if (!enemyIsParalyzed) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|" + enemyName[randomIndex]+ " used " + enemyAttackName + " and dealt " + enemyDamage + " damage to " + playerName + "|");
        }
        if (enemyIsParalyzed) {
            System.out.println("|--------------------------------------------|");
            System.out.println("|" + enemyName[randomIndex] + " is paralyzed and unable to attack!");
        }
        System.out.println("|--------------------------------------------|");
    }

    private static String getAttackName(int attack) {
        switch (attack) {
            case 1:
                return "Punch";
            case 2:
                return "Kick";
            case 3:
                return "Smackdown";
            case 4:
                return "Ultimate";
            default:
                return "Unknown Attack";
        }
    }

    private static void displayWinner(String playerName, int playerHP, String[] enemyName,int randomIndex,String RESET,String COLOR[] ) {
        if (playerHP <= 0) {
            System.out.println("\n"+COLOR[1] + playerName + " is defeated. " +COLOR[2]+ enemyName[randomIndex] + " is the winner!");
        } else {
            System.out.println("\n"+COLOR[1]+ enemyName[randomIndex] + " is defeated. "+COLOR[2] + playerName + " is the winner!");
        }
        System.out.print(RESET);
    }

}

            

            
