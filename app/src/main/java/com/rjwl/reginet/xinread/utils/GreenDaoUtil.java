package com.rjwl.reginet.xinread.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lei.greendao.DaoMaster;
import com.lei.greendao.DaoSession;
import com.lei.greendao.StudentDao;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Student;

/**
 * Created by Administrator on 2018/5/14.
 * 数据库辅助类
 */

public class GreenDaoUtil {
    private DaoMaster master;
    private DaoSession session;
    private SQLiteDatabase db;
    private StudentDao studentDao;
    private static GreenDaoUtil greenDaoUtil;

    private GreenDaoUtil(Context context) {
        db = new DaoMaster.DevOpenHelper(context,
                "read.db",
                null).getWritableDatabase();
        master = new DaoMaster(db);
        session = master.newSession();
        studentDao = session.getStudentDao();
    }

    public static GreenDaoUtil getDBUtil(Context context) {
        if (greenDaoUtil == null) {
            greenDaoUtil = new GreenDaoUtil(context);
        }
        return greenDaoUtil;
    }

    public void insertStu(Student student) {
        studentDao.insert(student);
    }

    public Student getStudetnById(int id) {
        Student s = studentDao.queryBuilder().
                where(StudentDao.Properties.Id.eq(id)).unique();
        if (s != null) {
            return s;
        }
        return null;
    }

    public void updataStudent(Student student){
        if (student == null) {
            return;
        }
        studentDao.update(student);
    }

    public void delAll() {
        studentDao.deleteAll();
    }

}
