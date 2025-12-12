# Supearlposition github
feel free to add any issues/bugs, a 1.2.2 modding coalition modjam submission.


# Supearlposition

The mod adds a new endgame multiblock machine, called the **Endergy Reactor**.  
It uses ender pearls to convert them to a new efficient fuel source, called the **Enriched Pearl**.

---

## Getting Started

Before creating the reactor one should first get at least **36 stacks of ender pearls** —  
**32 for the startup** of the reactor and **4 for the actual reactor recipe**.

Speaking of which, before setting up the reactor, you have to first craft its components.  
The central and most important component is the **Endergy Reactor Control block**. You only need **one**.

![image](https://media.forgecdn.net/attachments/description/null/description_c40bcb3f-8bf4-4907-a9cf-68332bc89acd.png)

Next up you will need **7 Endergy Reactor Parts** required to assemble the multiblock around the control, crafted like this:

![image](https://media.forgecdn.net/attachments/description/null/description_93c1f765-831c-4709-a83e-a6929571a707.png)

Once you have all the components, the reactor needs to be assembled:

![image](https://media.forgecdn.net/attachments/description/null/description_f8367075-7f81-4c45-a098-158b48b61645.png)

The reactor can be made in any **2×2 cube**, it doesn't matter where you place the control block — it has no effect on the function of the reactor.  
After constructing the reactor, **shift-right-click** the reactor control block with an **empty hand** to assemble it.

---

## Enriched Pearls

These are the fuel produced by the reactor. One pearl by itself is not notable — it has a fuel value a fraction of that of coal. Their true value is in their **quantity**.

They can be thrown to teleport any entity/block to a random position in proximity, similar to **chorus fruit**.

**They can also be used in the reactor to add to the number of pearls, adding 5 pearls instead of the usual 1.**

### Where is the drawback??

**THEY CANNOT BE STORED.**  
If one has **more than 10 pearls in one inventory slot**, OR in any chest/container, they will be **ejected from the top of the block**, causing mayhem.

![image](https://media.forgecdn.net/attachments/description/null/description_04501349-f641-413c-9773-3d3bde9761a9.png)

---

## How the Reactor Works

---

## 1. GUI

Once the reactor is assembled you can access its GUI by right-clicking any part of the reactor:

![image](https://media.forgecdn.net/attachments/description/null/description_3182b931-761a-4cff-bd55-f859229130c8.png)

Most of the statistics of the reactor can be seen by accessing the GUI.

In the middle of the panel there is a terminal which displays its stats.

If the reactor is **OFF**, it will display the number of pearls required for startup and that it is powered off.

Once the reactor is powered on, the progress of one **CYCLE** will be displayed, as well as its production speed.

The bar displays the number of pearls left until the next stage; hovering over the bar displays the current number of pearls in the reactor.

![image](https://media.forgecdn.net/attachments/description/null/description_db927f15-4a6c-4f39-b8f4-97a79981c376.png)

---

## 2. Production of Fuel

The reactor produces fuel based on the amount of pearls in the reactor; it has **5 stages**:

- **OFF:** The reactor produces no pearls; needs a startup amount.  
- **First stage:** 2.5 pearls/s  
- **Second stage:** 10 pearls/s  
- **Third stage:** 25 pearls/s  
- **Fourth stage:** 37.5 pearls/s  
- **Fifth stage:** 125 pearls/s  
- **MELTDOWN:** The reactor starts a meltdown.

Fuel can be extracted from any **bottom face** using a hopper or pipes from other mods (recommended).

---

## 3. The More You Have, The Worse It Gets

The reactor might seem like a good source of infinite fuel — and it is —  
however one should be careful if they are capable of handling so much fuel.

**It is worth noting that the reactor CANNOT be powered off.**  
Meaning that once the process is started, it is started.

If the fuel is not extracted, it will be stored in the internal inventory of the reactor, displayed on the right side.  
If those slots are full, **THE REACTOR WILL CONSUME ANY FUEL IT CANNOT FIT INTO THOSE SLOTS**, causing the number of pearls to rise indefinitely until those slots are emptied.

This can cause the reactor to progress through the stages by itself, eventually reaching meltdown.

---

## 4. The Meltdown

If the reactor reaches the meltdown stage, it will explode after a couple of seconds, leaving behind all the pearls it had without containment.

![image](https://media.forgecdn.net/attachments/description/null/description_4042de7a-144d-4cd6-8af8-44d99fe89d82.png)

The pearls will circle around their former containment, teleporting blocks and entities alike.
