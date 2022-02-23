package com.goodtime.bruaket.init;

import com.goodtime.bruaket.core.Bruaket;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Bruaket.MODID)
public class ModelMapper {

    @SideOnly(Side.CLIENT)
    public static void registerItemRender(Item item){
        buildSimpleJson("item", String.valueOf(item.getRegistryName()).split(":")[1]);
        ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, model);
    }

    public static void registerBlockRender(Block block){
        buildSimpleJson("block", String.valueOf(block.getRegistryName()).split(":")[1]);
        ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(new ItemBlock(block),0, model);

    }


    private static void buildSimpleJson(String type, String registerName){
        String resourcePath = Bruaket.class.getResource("/assets/"+Bruaket.MODID).getPath().replaceFirst("/","");


        if (resourcePath != null){

            FileWriter fw;

            switch (type){
                case "item":
                    String jsonPath = resourcePath+"/models/"+type+"/"+registerName+".json";

                    if(! new File(jsonPath).exists()){
                        String content = "{\n" +
                                "  \"parent\": \""+type+"/generated\",\n" +
                                "  \"textures\": {\n" +
                                "    \"layer0\": \""+Bruaket.MODID+":"+type+"/"+registerName+"\"\n" +
                                "  }\n" +
                                "}";

                        try {
                            fw = new FileWriter(jsonPath);
                            fw.write(content.toCharArray());
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    break;

                case "block":

                    String blockStatesPath = resourcePath+"/blockstates/"+registerName+".json";

                    String blockModelPath = resourcePath+"/models/"+type+"/"+registerName+".json";

                    String itemModelPath = resourcePath+"/models/item/"+registerName+".json";

                    if(! new File(blockModelPath).exists() && ! new File(blockStatesPath).exists() && ! new File(itemModelPath).exists()){
                        String blockStatesContent = "{\n" +
                                "    \"variants\": {\n" +
                                "        \"normal\": {\n" +
                                "            \"model\": \""+Bruaket.MODID+":"+registerName+"\"\n" +
                                "        }\n" +
                                "    }\n" +
                                "}";

                        String modelContent = "{\n" +
                                "  \"parent\": \"block/cube_all\",\n" +
                                "  \"textures\": {\n" +
                                "    \"all\": \""+Bruaket.MODID+":"+type+"/"+registerName+"\"\n" +
                                "  }\n" +
                                "}";

                        String itemModelContent = "{\n" +
                                "  \"parent\": \""+Bruaket.MODID+":"+type+"/"+registerName+"\"\n" +
                                "}";

                        try{
                            fw = new FileWriter(blockStatesPath);
                            fw.write(blockStatesContent.toCharArray());
                            fw.flush();

                            fw = new FileWriter(blockModelPath);
                            fw.write(modelContent.toCharArray());
                            fw.flush();

                            fw = new FileWriter(itemModelPath);
                            fw.write(itemModelContent.toCharArray());
                            fw.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

            }
        }
    }


}
