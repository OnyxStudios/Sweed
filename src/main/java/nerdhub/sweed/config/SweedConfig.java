package nerdhub.sweed.config;

public class SweedConfig {

    public double spreadChance = 0.5F; //chance for a plant to spread on random update, AFTER it has fully grown. 0 to disable spread
    public int spreadMinLightLevel = 7; //minimum light level required for new plants
    public int maxSpreadPlants = 3; //maximum amount of new plants that are created in one spreading attempt
    public int maxSpreadAttempts = 12; //maximum attempts to spread, less if successful or when there's sweed already at the tested position
    public boolean aggressiveSpread = true; //use a slower spreading algorithm and do not spread infinitely
}
