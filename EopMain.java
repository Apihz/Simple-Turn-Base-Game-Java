import java.util.Random; 
import java.util.Scanner;

public class Test2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[][] attacks = {
                {"Punch", "4"},
                {"Kick", "6"},
                {"Smackdown", "7"}
        };
            playTurn(scanner, attacks);
    }

    private static void playTurn(Scanner scanner, String[][] attacks) {
        
        String playerName = getPlayerName(scanner);
        int playerHP = 100;
       
        String enemyName = "Pikachu";
        int enemyHP = 100;

        while (playerHP > 0 && enemyHP > 0){
            
            displayStatus(playerName, playerHP, enemyName, enemyHP); 
            int playerPoisonCount = 0;
            boolean playerIsParalyzed = false;
            int enemyPoisonCount = 0;
            boolean enemyIsParalyzed = false;

            int playerAttack = getPlayerAttack(scanner, attacks);
            int playerDamage = calculateDamage(playerAttack);
            int playerAbility = getPlayerAbility(scanner);

            Random random = new Random();
            int enemyAttack = random.nextInt(attacks.length) + 1; // Random attack for the enemy
            int enemyDamage = calculateDamage(enemyAttack);
            int enemyAbility = random.nextInt(5) + 1; // Random ability for the enemy

            int playerMaxHP = 100;
            int healingAmount = 10;

            if (!playerIsParalyzed) {
                switch (playerAbility) {
                    case 1:
                        playerDamage = (int) (playerDamage * 1.5);
                        System.out.println(playerName + " used Boost! Attack power increased by 50%!");
                        break;
                    case 2:
                        enemyIsParalyzed = true;
                        System.out.println(playerName + " used Paralyze! " + enemyName + " will be unable to attack next turn!");
                        break;
                    case 3:
                        enemyPoisonCount = 4;
                        System.out.println(playerName + " used Poison! " + enemyName + " will lose 5 HP for 4 turns!");
                        break;
                    case 4:
                        enemyDamage = (int) (playerDamage * 0.7);
                        System.out.println(playerName + " used Defense! " + enemyName + "'s attack power is weakened by 30%!");
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

                enemyHP -= playerDamage;

                if (enemyPoisonCount > 0) {
                    enemyHP -= 5;
                    enemyPoisonCount--;
                }

            } 
            else {
                System.out.println(playerName + " is paralyzed and unable to attack!");
            }
            if (!enemyIsParalyzed) {
                switch (enemyAbility) {
                    case 1:
                        enemyDamage = (int) (enemyDamage * 1.5);
                        System.out.println(enemyName + " used Boost! Attack power increased by 50%!");
                        break;
                    case 2:
                        playerIsParalyzed = true;
                        System.out.println(enemyName + " used Paralyze! " + playerName + " will be unable to attack next turn!");
                        break;
                    case 3:
                        playerPoisonCount = 4;
                        System.out.println(enemyName + " used Poison! " + playerName + " will lose 5 HP for 4 turns!");
                        break;
                    case 4:
                        playerDamage = (int) (enemyDamage * 0.7);
                        System.out.println(enemyName + " used Smokescreen! " + playerName + "'s attack power is weakened by 30%!");
                        break;
                    case 5:
                        if (enemyHP < playerMaxHP) {
                            if (enemyHP + healingAmount > playerMaxHP) {
                                enemyHP = playerMaxHP;
                            } else {
                                enemyHP += healingAmount;
                            }
                            System.out.println(enemyName + " used Heal! " + enemyName + " recovers " + healingAmount + " HP!");
                        } else {
                            System.out.println(enemyName + " is already at full health!");
                        }
                        break;
                    default:
                        System.out.println(enemyName + " used an invalid ability!");
                }

                playerHP -= enemyDamage;

                if (playerPoisonCount > 0) {
                    playerHP -= 5;
                    playerPoisonCount--;
                }

            } else {
                System.out.println(enemyName + " is paralyzed and unable to attack!");
            }

            displayAttack(playerName, enemyName, playerAttack, playerDamage, enemyAttack, enemyDamage, playerIsParalyzed, enemyIsParalyzed);

            playerIsParalyzed = false;
            enemyIsParalyzed = false;
            
        }
        displayWinner(playerName, playerHP, enemyName, enemyHP);
    }

    private static String getPlayerName(Scanner scanner) {
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    private static void displayStatus(String playerName, int playerHP, String enemyName, int enemyHP) {
        System.out.println("\n" + playerName + " || HP: " + playerHP);
        System.out.println(enemyName + " || HP: " + enemyHP);
    }

    private static int getPlayerAttack(Scanner scanner, String[][] attacks) {
        int attack;
        do {
            System.out.println("\nAttack list:");
            for (int i = 0; i < attacks.length; i++) {
                System.out.println((i + 1) + ". " + attacks[i][0] + " (Dmg: " + attacks[i][1] + ")");
            }
            System.out.print("Select attack: ");
            attack = scanner.nextInt();

            if (attack < 1 || attack > attacks.length) {
                System.out.println("Invalid selection. Please choose a valid attack (1-" + attacks.length + ").");
            }

        } while (attack < 1 || attack > attacks.length);

        return attack;
    }

    private static int calculateDamage(int attack) {
        switch (attack) {
            case 1:
                return 4;
            case 2:
                return 6;
            case 3:
                return 7;
            default:
                return 0;
        }
    }

    private static int getPlayerAbility(Scanner scanner) {
        int ability;
        do {
            System.out.println("\nAbility list:");
            System.out.println("1. Boost (Dmg: Multiply 1.5x)");
            System.out.println("2. Paralyze enemy (Enemy turn will be skipped)");
            System.out.println("3. Poison (Enemy health drops 5 for 4 turns)");
            System.out.println("4. Defense (Enemy attack multiplied by 0.7)");
            System.out.println("5. Heal (Health +10)");
            System.out.print("Select your ability: ");
            ability = scanner.nextInt();
            System.out.println();

            if (ability < 1 || ability > 5) {
                System.out.println("Invalid selection. Please choose a valid ability (1-5).");
            }

        } while (ability < 1 || ability > 5);

        return ability;
    }

    private static void displayAttack(String playerName, String enemyName, int playerAttack, int playerDamage, int enemyAttack, int enemyDamage, boolean playerIsParalyzed, boolean enemyIsParalyzed) {
        String playerAttackName = getAttackName(playerAttack);
        String enemyAttackName = getAttackName(enemyAttack);
        if(!playerIsParalyzed){
            System.out.println("|----------------------------------");
            System.out.println("|" + playerName + " used " + playerAttackName + " and dealt " + playerDamage + " damage to " + enemyName);
        }
        if (!enemyIsParalyzed) {
            System.out.println("|----------------------------------");
            System.out.println("|" + enemyName + " used " + enemyAttackName + " and dealt " + enemyDamage + " damage to " + playerName);
        }
        if (playerIsParalyzed) {
            System.out.println("|----------------------------------");
            System.out.println("|" + playerName + " is paralyzed and unable to attack!");
        }
        if (enemyIsParalyzed) {
            System.out.println("|----------------------------------");
            System.out.println("|" + enemyName + " is paralyzed and unable to attack!");
        }
        System.out.println("|----------------------------------");
    }

    private static String getAttackName(int attack) {
        switch (attack) {
            case 1:
                return "Punch";
            case 2:
                return "Kick";
            case 3:
                return "Smackdown";
            default:
                return "Unknown Attack";
        }
    }

    private static void displayWinner(String playerName, int playerHP, String enemyName, int enemyHP) {
        if (playerHP <= 0) {
            System.out.println("\n" + playerName + " is defeated. " + enemyName + " is the winner!");
        } else {
            System.out.println("\n" + enemyName + " is defeated. " + playerName + " is the winner!");
        }
    }
}
