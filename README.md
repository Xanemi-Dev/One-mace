# One Mace plugin

This is a minimal PaperMC plugin that ensures every player has at most one special "Mace" item.

Build with Maven (Java 17). Update the paper-api version in pom.xml if necessary to match your Paper server.

Usage:
- Drop the compiled JAR into your server's plugins/ folder.
- Players will be given a Mace on join if they don't have one.
- Additional Mace items in inventory or picked up will be removed/cancelled so only one remains.
- Operators can use /givemace to get one.
