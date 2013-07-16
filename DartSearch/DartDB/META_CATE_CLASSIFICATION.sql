/*
create table META_CATEGORY_1
(
  ID    INTEGER,
  DNAME VARCHAR(100)
);

create table META_CATEGORY_2
(
  PID   INTEGER,
  ID    INTEGER,
  DNAME VARCHAR(100)
);

create table META_CATE_CLASSIFICATION
(
  ID        INTEGER not null,
  CATEGORY1 INTEGER,
  CATEGORY2 INTEGER,
  CATEGORY3 INTEGER,
  ONTOURI   VARCHAR(200),
  ONTONAME  VARCHAR(100)
);

create table Category_ID_Table
(
  ID INTEGER not null
);

alter table META_CATE_CLASSIFICATION
  add primary key (ID)
  using index 

*/

insert into META_CATEGORY_1 (ID, DNAME)
values (427, '呼吸专题');
insert into META_CATEGORY_1 (ID, DNAME)
values (428, '突发公卫');
insert into META_CATEGORY_1 (ID, DNAME)
values (429, '神经专题');
insert into META_CATEGORY_1 (ID, DNAME)
values (430, '传染病专题');
insert into META_CATEGORY_1 (ID, DNAME)
values (431, '内科病证');
insert into META_CATEGORY_1 (ID, DNAME)
values (432, '肿瘤');
insert into META_CATEGORY_1 (ID, DNAME)
values (433, '儿科病证');
insert into META_CATEGORY_1 (ID, DNAME)
values (434, '骨伤科病证');
insert into META_CATEGORY_1 (ID, DNAME)
values (435, '针灸数据库');
insert into META_CATEGORY_1 (ID, DNAME)
values (436, '妇科病证');
insert into META_CATEGORY_1 (ID, DNAME)
values (437, '口腔科病证');
insert into META_CATEGORY_1 (ID, DNAME)
values (438, '五官科病证');
insert into META_CATEGORY_1 (ID, DNAME)
values (439, '中医证候');
insert into META_CATEGORY_1 (ID, DNAME)
values (440, '传染病');
commit;

insert into META_CATEGORY_2 (PID, ID, DNAME)
values (140, 159, '测试子专题1');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (140, 160, '测试子专题2');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (427, 441, '呼吸专题');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (428, 478, '突发公卫');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (429, 444, '神经专题');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (430, 445, '病毒性肝炎');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 446, '糖尿病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 447, '肾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 448, '老年性痴呆');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 449, '中风');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 450, '高血压');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 451, '冠心病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 452, '消化系统疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 453, '心血管疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 454, '神经系统疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 455, '免疫系统');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (432, 456, '肿瘤');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (433, 457, '新生儿疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (433, 458, '婴儿疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (433, 459, '学龄前儿童疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (433, 460, '儿童疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (434, 461, '股骨头坏死');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (434, 462, '颈椎病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (435, 463, '针灸');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (434, 464, '骨科疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (436, 465, '月经病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (436, 466, '更年期综合证');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (436, 467, '不孕症');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (436, 468, '乳腺增生');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (436, 469, '妇科疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (437, 470, '口腔疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (438, 471, '眼科疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (439, 472, '中医证候');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 473, '哮喘病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (440, 474, '艾滋病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (431, 475, '泌尿系统疾病');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (434, 476, '骨质疏松');
insert into META_CATEGORY_2 (PID, ID, DNAME)
values (140, 225, '测试子专题3');
commit;

insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (477, 427, 441, null, 'http://dart.zju.edu.cn/dartcore/dart#呼吸专题', '呼吸专题');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (479, 428, 478, null, 'http://dart.zju.edu.cn/dartcore/dart#突发公卫专题', '突发公卫专题');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (480, 429, 444, null, 'http://dart.zju.edu.cn/dartcore/dart#神经专题', '神经专题');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (481, 430, 445, null, 'http://dart.zju.edu.cn/dartcore/dart#病毒性肝炎', '病毒性肝炎');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (482, 431, 446, null, 'http://dart.zju.edu.cn/dartcore/dart#糖尿病', '糖尿病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (483, 431, 447, null, 'http://dart.zju.edu.cn/dartcore/dart#肾病', '肾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (484, 431, 448, null, 'http://dart.zju.edu.cn/dartcore/dart#老年性痴呆', '老年性痴呆');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (485, 431, 449, null, 'http://dart.zju.edu.cn/dartcore/dart#中风', '中风');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (486, 431, 450, null, 'http://dart.zju.edu.cn/dartcore/dart#高血压', '高血压');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (487, 431, 451, null, 'http://dart.zju.edu.cn/dartcore/dart#冠心病', '冠心病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (488, 431, 452, null, 'http://dart.zju.edu.cn/dartcore/dart#消化系统', '消化系统');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (489, 431, 453, null, 'http://dart.zju.edu.cn/dartcore/dart#心血管疾病', '心血管疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (490, 431, 454, null, 'http://dart.zju.edu.cn/dartcore/dart#神经系统疾病', '神经系统疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (491, 431, 455, null, 'http://dart.zju.edu.cn/dartcore/dart#免疫系统', '免疫系统');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (492, 431, 473, null, 'http://dart.zju.edu.cn/dartcore/dart#哮喘病', '哮喘病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (493, 431, 475, null, 'http://dart.zju.edu.cn/dartcore/dart#泌尿系统疾病', '泌尿系统疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (494, 432, 456, null, 'http://dart.zju.edu.cn/dartcore/dart#肿瘤', '肿瘤');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (495, 433, 457, null, 'http://dart.zju.edu.cn/dartcore/dart#儿童疾病', '儿童疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (496, 433, 458, null, 'http://dart.zju.edu.cn/dartcore/dart#婴儿疾病', '婴儿疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (497, 433, 459, null, 'http://dart.zju.edu.cn/dartcore/dart#学龄前儿童疾病', '学龄前儿童疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (498, 433, 460, null, 'http://dart.zju.edu.cn/dartcore/dart#儿童疾病', '儿童疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (499, 434, 461, null, 'http://dart.zju.edu.cn/dartcore/dart#股骨头坏死', '股骨头坏死');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (500, 434, 462, null, 'http://dart.zju.edu.cn/dartcore/dart#颈椎病', '颈椎病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (501, 434, 464, null, 'http://dart.zju.edu.cn/dartcore/dart#骨科疾病', '骨科疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (502, 434, 476, null, 'http://dart.zju.edu.cn/dartcore/dart#骨质疏松', '骨质疏松');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (503, 435, 463, null, 'http://dart.zju.edu.cn/dartcore/dart#针灸疗法', '针灸疗法');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (504, 436, 465, null, 'http://dart.zju.edu.cn/dartcore/dart#月经病', '月经病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (505, 436, 466, null, 'http://dart.zju.edu.cn/dartcore/dart#更年期综合症', '更年期综合症');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (506, 436, 467, null, 'http://dart.zju.edu.cn/dartcore/dart#不孕症', '不孕症');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (507, 436, 468, null, 'http://dart.zju.edu.cn/dartcore/dart#乳腺增生', '乳腺增生');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (508, 436, 469, null, 'http://dart.zju.edu.cn/dartcore/dart#妇科疾病', '妇科疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (509, 437, 470, null, 'http://dart.zju.edu.cn/dartcore/dart#口腔疾病', '口腔疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (510, 438, 471, null, 'http://dart.zju.edu.cn/dartcore/dart#眼科疾病', '眼科疾病');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (511, 439, 472, null, 'http://dart.zju.edu.cn/dartcore/dart#中医病证', '中医病证');
insert into META_CATE_CLASSIFICATION (ID, CATEGORY1, CATEGORY2, CATEGORY3, ONTOURI, ONTONAME)
values (512, 440, 474, null, 'http://dart.zju.edu.cn/dartcore/dart#艾滋病', '艾滋病');
commit;



