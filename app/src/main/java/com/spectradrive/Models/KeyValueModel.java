package com.spectradrive.Models;

public class KeyValueModel<TKey, TVal> {
   public TKey Key;
   public TVal Val;

   public KeyValueModel(TKey key, TVal val){
       Key=key;
       Val = val;
   }
}
