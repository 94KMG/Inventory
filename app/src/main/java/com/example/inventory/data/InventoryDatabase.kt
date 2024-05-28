package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//데이터베이스에 포함될 엔티티(테이블)를 지정합니다.
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    //클래스의 정적 멤버처럼 동작하는 객체를 정의합니다.
    // 이는 클래스당 하나만 존재하며, 인스턴스를 생성하지 않고도 접근할 수 있습니다.
    companion object {
        //변수를 휘발성으로 선언합니다. 이는 여러 스레드에서 변수의 최신 값을 즉시 읽고 쓸 수 있도록 보장합니다.
        // 따라서, 한 스레드가 Instance를 변경하면 다른 스레드에서도 즉시 변경된 값을 볼 수 있습니다.
        @Volatile
        private var Instance: InventoryDatabase? = null
        fun getDatabase(context: Context): InventoryDatabase {
            //Instance가 null이면 synchronized 블록 내부의 코드를 실행합니다
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    //생성된 인스턴스를 Instance 변수에 저장합니다.
                    Instance = instance
                    // 이 후, Instance가 null이 아니게 되어 다음 호출부터는 이미 생성된 인스턴스를 반환합니다.
                    return instance
            }
        }
    }
}