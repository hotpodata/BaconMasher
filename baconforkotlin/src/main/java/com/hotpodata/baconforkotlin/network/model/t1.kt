package com.hotpodata.baconforkotlin.network.model

/**
 * Created by jdrotos on 11/29/15.
 */
class t1 : ThingData{

    var subreddit: String? = null
    var ups: Int? = null
    var body: String? = null
    var body_html: String? = null
    var replies: Thing? = null

//    "subreddit_id": "t5_2sbq3",
//    "banned_by": null,
//    "removal_reason": null,
//    "link_id": "t3_3o2ion",
//    "likes": null,
//    "replies": {
//        "kind": "Listing",
//        "data": {
//            "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//            "children": [
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": {
//                "kind": "Listing",
//                "data": {
//                "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                "children": [
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": {
//                    "kind": "Listing",
//                    "data": {
//                    "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                    "children": [
//                    {
//                        "kind": "t1",
//                        "data": {
//                        "subreddit_id": "t5_2sbq3",
//                        "banned_by": null,
//                        "removal_reason": null,
//                        "link_id": "t3_3o2ion",
//                        "likes": null,
//                        "replies": "",
//                        "user_reports": [
//
//                        ],
//                        "saved": false,
//                        "id": "cweb8h8",
//                        "gilded": 0,
//                        "archived": false,
//                        "report_reasons": null,
//                        "author": "millamb4",
//                        "parent_id": "t1_cwchdaw",
//                        "score": 1,
//                        "approved_by": null,
//                        "controversiality": 0,
//                        "body": "Please do. I know that Obama also pointed out this matter in his weekly address this week. ",
//                        "edited": false,
//                        "author_flair_css_class": null,
//                        "downs": 0,
//                        "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;Please do. I know that Obama also pointed out this matter in his weekly address this week. &lt;\/p&gt;\n&lt;\/div&gt;",
//                        "subreddit": "EarthPorn",
//                        "score_hidden": false,
//                        "name": "t1_cweb8h8",
//                        "created": 1445953111,
//                        "author_flair_text": null,
//                        "created_utc": 1445924311,
//                        "distinguished": null,
//                        "mod_reports": [
//
//                        ],
//                        "num_reports": null,
//                        "ups": 1
//                    }
//                    }
//                    ],
//                    "after": null,
//                    "before": null
//                }
//                },
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cwchdaw",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "myperfectyou",
//                    "parent_id": "t1_cvtx86j",
//                    "score": 5,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "This is awful, and the first I've heard of it. I will do my best to get the news out.",
//                    "edited": false,
//                    "author_flair_css_class": null,
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;This is awful, and the first I&amp;#39;ve heard of it. I will do my best to get the news out.&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cwchdaw",
//                    "created": 1445829542,
//                    "author_flair_text": null,
//                    "created_utc": 1445800742,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 5
//                }
//                },
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": "",
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cvtynp6",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "goddamnstupidmofo",
//                    "parent_id": "t1_cvtx86j",
//                    "score": 6,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "My pleasure - I hope that folks can make enough noise about this to get it re-funded!\n\n[Here's a more detailed article from Slate about the fund and what it provides](http:\/\/www.slate.com\/articles\/health_and_science\/science\/2015\/09\/land_and_water_conservation_fund_is_out_of_budget_and_will_expire.html).\n\n",
//                    "edited": false,
//                    "author_flair_css_class": null,
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;My pleasure - I hope that folks can make enough noise about this to get it re-funded!&lt;\/p&gt;\n\n&lt;p&gt;&lt;a href=\"http:\/\/www.slate.com\/articles\/health_and_science\/science\/2015\/09\/land_and_water_conservation_fund_is_out_of_budget_and_will_expire.html\"&gt;Here&amp;#39;s a more detailed article from Slate about the fund and what it provides&lt;\/a&gt;.&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cvtynp6",
//                    "created": 1444443573,
//                    "author_flair_text": null,
//                    "created_utc": 1444414773,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 6
//                }
//                }
//                ],
//                "after": null,
//                "before": null
//            }
//            },
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cvtx86j",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "millamb4",
//                "parent_id": "t1_cvtww3m",
//                "score": 31,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "Beautifully said and great info. Thank you for this. \n\n**EDIT: Also putting this here to grab attention:**\n\nThis guy is clearly wanting to destroy all conservation efforts and public lands:\nhttp:\/\/www.eenews.net\/stories\/1060027594",
//                "edited": 1447097097,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;Beautifully said and great info. Thank you for this. &lt;\/p&gt;\n\n&lt;p&gt;&lt;strong&gt;EDIT: Also putting this here to grab attention:&lt;\/strong&gt;&lt;\/p&gt;\n\n&lt;p&gt;This guy is clearly wanting to destroy all conservation efforts and public lands:\n&lt;a href=\"http:\/\/www.eenews.net\/stories\/1060027594\"&gt;http:\/\/www.eenews.net\/stories\/1060027594&lt;\/a&gt;&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cvtx86j",
//                "created": 1444441443,
//                "author_flair_text": null,
//                "created_utc": 1444412643,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 31
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": {
//                "kind": "Listing",
//                "data": {
//                "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                "children": [
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": "",
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cwiyqwh",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "Wilreadit",
//                    "parent_id": "t1_cwiarbr",
//                    "score": 4,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "You can lead some kind of conservation effort in your own area. Assist afforestation, fight deforestation, fight corporations, fight the use of plastic. Just do what the hippies do, and encourage participation from motivated public",
//                    "edited": false,
//                    "author_flair_css_class": null,
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;You can lead some kind of conservation effort in your own area. Assist afforestation, fight deforestation, fight corporations, fight the use of plastic. Just do what the hippies do, and encourage participation from motivated public&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cwiyqwh",
//                    "created": 1446278043,
//                    "author_flair_text": null,
//                    "created_utc": 1446249243,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 4
//                }
//                }
//                ],
//                "after": null,
//                "before": null
//            }
//            },
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cwiarbr",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "JFSOCC",
//                "parent_id": "t1_cvtww3m",
//                "score": 9,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "What can I do as a non-American who cares?",
//                "edited": false,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;What can I do as a non-American who cares?&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cwiarbr",
//                "created": 1446239010,
//                "author_flair_text": null,
//                "created_utc": 1446210210,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 9
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": "",
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cwmw00d",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "austenite",
//                "parent_id": "t1_cvtww3m",
//                "score": 5,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "You need to crosspost this in more subs\n",
//                "edited": false,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;You need to crosspost this in more subs&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cwmw00d",
//                "created": 1446590869,
//                "author_flair_text": null,
//                "created_utc": 1446562069,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 5
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": "",
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cwnd143",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "mars20",
//                "parent_id": "t1_cvtww3m",
//                "score": 5,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "The US nature and national parks with their infrastructure were THE single feature that made me love this country an wanting to come back and travel again through this untouched vast wilderness and beauty.\n\nPlease don't kill it. Americans, stand up and tell your representatives to preserve this!",
//                "edited": false,
//                "author_flair_css_class": "Camera",
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;The US nature and national parks with their infrastructure were THE single feature that made me love this country an wanting to come back and travel again through this untouched vast wilderness and beauty.&lt;\/p&gt;\n\n&lt;p&gt;Please don&amp;#39;t kill it. Americans, stand up and tell your representatives to preserve this!&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cwnd143",
//                "created": 1446614997,
//                "author_flair_text": null,
//                "created_utc": 1446586197,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 5
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": {
//                "kind": "Listing",
//                "data": {
//                "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                "children": [
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": "",
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cwuem15",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "millamb4",
//                    "parent_id": "t1_cwp2fbj",
//                    "score": 1,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "No idea, but this is some new developments:\n\nhttp:\/\/www.eenews.net\/stories\/1060027594",
//                    "edited": false,
//                    "author_flair_css_class": null,
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;No idea, but this is some new developments:&lt;\/p&gt;\n\n&lt;p&gt;&lt;a href=\"http:\/\/www.eenews.net\/stories\/1060027594\"&gt;http:\/\/www.eenews.net\/stories\/1060027594&lt;\/a&gt;&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cwuem15",
//                    "created": 1447125986,
//                    "author_flair_text": null,
//                    "created_utc": 1447097186,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 1
//                }
//                },
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": "",
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cxgsfou",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "brap0",
//                    "parent_id": "t1_cwp2fbj",
//                    "score": 1,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "How long does it take a bill to get out of a subcommittee? Ten minutes, if they want it to. Forever, if a majority member wants to sit on it.",
//                    "edited": false,
//                    "author_flair_css_class": "Camera",
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;How long does it take a bill to get out of a subcommittee? Ten minutes, if they want it to. Forever, if a majority member wants to sit on it.&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cxgsfou",
//                    "created": 1448839743,
//                    "author_flair_text": null,
//                    "created_utc": 1448810943,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 1
//                }
//                }
//                ],
//                "after": null,
//                "before": null
//            }
//            },
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cwp2fbj",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "Jakhil",
//                "parent_id": "t1_cvtww3m",
//                "score": 3,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "It appears legislature to reinstate the LWCF is caught up in subcommittee's. Does anyone know how long it takes to get out of a subcommittee and onto the floor? If anyone has any contacts at the LWCF it might be cool to help them set up an AMA with some of the senators backing positive legislature to bring some attention on the topic. The only reason I know of it was the sheer luck of seeing this post.",
//                "edited": false,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;It appears legislature to reinstate the LWCF is caught up in subcommittee&amp;#39;s. Does anyone know how long it takes to get out of a subcommittee and onto the floor? If anyone has any contacts at the LWCF it might be cool to help them set up an AMA with some of the senators backing positive legislature to bring some attention on the topic. The only reason I know of it was the sheer luck of seeing this post.&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cwp2fbj",
//                "created": 1446727077,
//                "author_flair_text": null,
//                "created_utc": 1446698277,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 3
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": "",
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cwuehoo",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "millamb4",
//                "parent_id": "t1_cvtww3m",
//                "score": 1,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "**This guy is clearly wanting to destroy all conservation efforts and public lands:**\n\nhttp:\/\/www.eenews.net\/stories\/1060027594",
//                "edited": false,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;&lt;strong&gt;This guy is clearly wanting to destroy all conservation efforts and public lands:&lt;\/strong&gt;&lt;\/p&gt;\n\n&lt;p&gt;&lt;a href=\"http:\/\/www.eenews.net\/stories\/1060027594\"&gt;http:\/\/www.eenews.net\/stories\/1060027594&lt;\/a&gt;&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cwuehoo",
//                "created": 1447125818,
//                "author_flair_text": null,
//                "created_utc": 1447097018,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 1
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": "",
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cw6iy7m",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "SadSillyWorld",
//                "parent_id": "t1_cvtww3m",
//                "score": 1,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "I filled out the petition online. Best of luck in getting this decision reversed. It is an outrage.",
//                "edited": false,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;I filled out the petition online. Best of luck in getting this decision reversed. It is an outrage.&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cw6iy7m",
//                "created": 1445387910,
//                "author_flair_text": null,
//                "created_utc": 1445359110,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 1
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": {
//                "kind": "Listing",
//                "data": {
//                "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                "children": [
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": {
//                    "kind": "Listing",
//                    "data": {
//                    "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                    "children": [
//                    {
//                        "kind": "t1",
//                        "data": {
//                        "subreddit_id": "t5_2sbq3",
//                        "banned_by": null,
//                        "removal_reason": null,
//                        "link_id": "t3_3o2ion",
//                        "likes": null,
//                        "replies": {
//                        "kind": "Listing",
//                        "data": {
//                        "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                        "children": [
//                        {
//                            "kind": "t1",
//                            "data": {
//                            "subreddit_id": "t5_2sbq3",
//                            "banned_by": null,
//                            "removal_reason": null,
//                            "link_id": "t3_3o2ion",
//                            "likes": null,
//                            "replies": {
//                            "kind": "Listing",
//                            "data": {
//                            "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                            "children": [
//                            {
//                                "kind": "t1",
//                                "data": {
//                                "subreddit_id": "t5_2sbq3",
//                                "banned_by": null,
//                                "removal_reason": null,
//                                "link_id": "t3_3o2ion",
//                                "likes": null,
//                                "replies": "",
//                                "user_reports": [
//
//                                ],
//                                "saved": false,
//                                "id": "cxbhjwn",
//                                "gilded": 0,
//                                "archived": false,
//                                "report_reasons": null,
//                                "author": "uscmissinglink",
//                                "parent_id": "t1_cxbhduw",
//                                "score": 1,
//                                "approved_by": null,
//                                "controversiality": 0,
//                                "body": "Possibly true, but http:\/\/i.imgur.com\/umSCC2A.jpg",
//                                "edited": false,
//                                "author_flair_css_class": "Camera",
//                                "downs": 0,
//                                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;Possibly true, but &lt;a href=\"http:\/\/i.imgur.com\/umSCC2A.jpg\"&gt;http:\/\/i.imgur.com\/umSCC2A.jpg&lt;\/a&gt;&lt;\/p&gt;\n&lt;\/div&gt;",
//                                "subreddit": "EarthPorn",
//                                "score_hidden": false,
//                                "name": "t1_cxbhjwn",
//                                "created": 1448410543,
//                                "author_flair_text": null,
//                                "created_utc": 1448381743,
//                                "distinguished": null,
//                                "mod_reports": [
//
//                                ],
//                                "num_reports": null,
//                                "ups": 1
//                            }
//                            }
//                            ],
//                            "after": null,
//                            "before": null
//                        }
//                        },
//                            "user_reports": [
//
//                            ],
//                            "saved": false,
//                            "id": "cxbhduw",
//                            "gilded": 0,
//                            "archived": false,
//                            "report_reasons": null,
//                            "author": "millamb4",
//                            "parent_id": "t1_cxbgogo",
//                            "score": 1,
//                            "approved_by": null,
//                            "controversiality": 0,
//                            "body": "Purchasing lands though is often a critical piece of taking care of existing public lands. Consider this: https:\/\/www.tpl.org\/media-room\/new-acquisition-improve-access-mt-rainier-national-park\n\nThere are quite a few private lands within National Parks (e.g. Sequoia National Park, Mineral King) and the ability to purchase these lands and protect them under the NPS jurisdiction is an absolute necessity in my opinion. Otherwise, you may find a luxurious house in middle of a wilderness area because of private inholding. \n\nHere you can see all the different projects funded fully or partially by LWCF, and I see a number of existing sites funded by this, so I am not sure if I buy the claim from The Hill. But even if it's not about maintaining existing lands, it's still just as critical considering that the NPS for example has less than 1\/10th of 1% of the federal budget. \nhttp:\/\/www.lwcfcoalition.org\/usa-conservation.html",
//                            "edited": false,
//                            "author_flair_css_class": null,
//                            "downs": 0,
//                            "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;Purchasing lands though is often a critical piece of taking care of existing public lands. Consider this: &lt;a href=\"https:\/\/www.tpl.org\/media-room\/new-acquisition-improve-access-mt-rainier-national-park\"&gt;https:\/\/www.tpl.org\/media-room\/new-acquisition-improve-access-mt-rainier-national-park&lt;\/a&gt;&lt;\/p&gt;\n\n&lt;p&gt;There are quite a few private lands within National Parks (e.g. Sequoia National Park, Mineral King) and the ability to purchase these lands and protect them under the NPS jurisdiction is an absolute necessity in my opinion. Otherwise, you may find a luxurious house in middle of a wilderness area because of private inholding. &lt;\/p&gt;\n\n&lt;p&gt;Here you can see all the different projects funded fully or partially by LWCF, and I see a number of existing sites funded by this, so I am not sure if I buy the claim from The Hill. But even if it&amp;#39;s not about maintaining existing lands, it&amp;#39;s still just as critical considering that the NPS for example has less than 1\/10th of 1% of the federal budget. \n&lt;a href=\"http:\/\/www.lwcfcoalition.org\/usa-conservation.html\"&gt;http:\/\/www.lwcfcoalition.org\/usa-conservation.html&lt;\/a&gt;&lt;\/p&gt;\n&lt;\/div&gt;",
//                            "subreddit": "EarthPorn",
//                            "score_hidden": false,
//                            "name": "t1_cxbhduw",
//                            "created": 1448410297,
//                            "author_flair_text": null,
//                            "created_utc": 1448381497,
//                            "distinguished": null,
//                            "mod_reports": [
//
//                            ],
//                            "num_reports": null,
//                            "ups": 1
//                        }
//                        }
//                        ],
//                        "after": null,
//                        "before": null
//                    }
//                    },
//                        "user_reports": [
//
//                        ],
//                        "saved": false,
//                        "id": "cxbgogo",
//                        "gilded": 0,
//                        "archived": false,
//                        "report_reasons": null,
//                        "author": "uscmissinglink",
//                        "parent_id": "t1_cxbg0tu",
//                        "score": 1,
//                        "approved_by": null,
//                        "controversiality": 0,
//                        "body": "From the [LWCF website](http:\/\/lwcfcoalition.org\/):\n\n&gt;The Land and Water Conservation Fund (LWCF) is the federal program to conserve irreplaceable lands and improve outdoor recreation opportunities throughout the nation. The program works in partnership with state and local efforts to acquire and protect inholdings and expansions in our national parks, national wildlife refuges, national forests, national trails, and BLM areas. LWCF grants to states support the acquisition and development of state and local parks and recreational facilities. But the program has been chronically underfunded leading to a number of missed opportunities for investing in important areas.\n\nInholdings are privately controlled lands surrounded by federal land. LWCF was designed to buy those lands - and lands adjacent to existing federal lands - to expand federal acreage.\n\nAnd from [The Hill](http:\/\/thehill.com\/blogs\/pundits-blog\/energy-environment\/254755-why-the-land-and-water-conservation-fund-needs-to-be):\n\n&gt;Some have called the LWCF \"the most popular parks program you've never heard of.\" It devotes up to $900 million each year from offshore oil and gas revenues for conservation and recreation purposes at both the federal and state level. In its current form, however, federal LWCF funds can only be used for acquiring more public lands. They cannot be used for the care and maintenance of existing federal lands.\n\n&gt;In other words, the LWCF allows the federal government to purchase more land, but it does not provide any means of taking care of those lands \u2014 or the critical needs that exist on the hundreds of millions of acres the federal government already owns.",
//                        "edited": false,
//                        "author_flair_css_class": "Camera",
//                        "downs": 0,
//                        "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;From the &lt;a href=\"http:\/\/lwcfcoalition.org\/\"&gt;LWCF website&lt;\/a&gt;:&lt;\/p&gt;\n\n&lt;blockquote&gt;\n&lt;p&gt;The Land and Water Conservation Fund (LWCF) is the federal program to conserve irreplaceable lands and improve outdoor recreation opportunities throughout the nation. The program works in partnership with state and local efforts to acquire and protect inholdings and expansions in our national parks, national wildlife refuges, national forests, national trails, and BLM areas. LWCF grants to states support the acquisition and development of state and local parks and recreational facilities. But the program has been chronically underfunded leading to a number of missed opportunities for investing in important areas.&lt;\/p&gt;\n&lt;\/blockquote&gt;\n\n&lt;p&gt;Inholdings are privately controlled lands surrounded by federal land. LWCF was designed to buy those lands - and lands adjacent to existing federal lands - to expand federal acreage.&lt;\/p&gt;\n\n&lt;p&gt;And from &lt;a href=\"http:\/\/thehill.com\/blogs\/pundits-blog\/energy-environment\/254755-why-the-land-and-water-conservation-fund-needs-to-be\"&gt;The Hill&lt;\/a&gt;:&lt;\/p&gt;\n\n&lt;blockquote&gt;\n&lt;p&gt;Some have called the LWCF &amp;quot;the most popular parks program you&amp;#39;ve never heard of.&amp;quot; It devotes up to $900 million each year from offshore oil and gas revenues for conservation and recreation purposes at both the federal and state level. In its current form, however, federal LWCF funds can only be used for acquiring more public lands. They cannot be used for the care and maintenance of existing federal lands.&lt;\/p&gt;\n\n&lt;p&gt;In other words, the LWCF allows the federal government to purchase more land, but it does not provide any means of taking care of those lands \u2014 or the critical needs that exist on the hundreds of millions of acres the federal government already owns.&lt;\/p&gt;\n&lt;\/blockquote&gt;\n&lt;\/div&gt;",
//                        "subreddit": "EarthPorn",
//                        "score_hidden": false,
//                        "name": "t1_cxbgogo",
//                        "created": 1448409224,
//                        "author_flair_text": null,
//                        "created_utc": 1448380424,
//                        "distinguished": null,
//                        "mod_reports": [
//
//                        ],
//                        "num_reports": null,
//                        "ups": 1
//                    }
//                    }
//                    ],
//                    "after": null,
//                    "before": null
//                }
//                },
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cxbg0tu",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "millamb4",
//                    "parent_id": "t1_cx1qijb",
//                    "score": 1,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "Genuine question, do you have a source for your claims? \n\nYour claim that \"LWCF has exactly fuck to do with existing public lands\" is false. You can do a Google search and see that LWCF has funded projects in EXISTING National Parks and Forests. For example, one project in Yellowstone National Park and one project in Mount Rainier National Park (repairing a major road that had been washed out by mud) were completed using this fund.",
//                    "edited": false,
//                    "author_flair_css_class": null,
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;Genuine question, do you have a source for your claims? &lt;\/p&gt;\n\n&lt;p&gt;Your claim that &amp;quot;LWCF has exactly fuck to do with existing public lands&amp;quot; is false. You can do a Google search and see that LWCF has funded projects in EXISTING National Parks and Forests. For example, one project in Yellowstone National Park and one project in Mount Rainier National Park (repairing a major road that had been washed out by mud) were completed using this fund.&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cxbg0tu",
//                    "created": 1448408192,
//                    "author_flair_text": null,
//                    "created_utc": 1448379392,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 1
//                }
//                },
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": "",
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cxgsjfs",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "brap0",
//                    "parent_id": "t1_cx1qijb",
//                    "score": 1,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "\"Mount Si, Washington\u2019s most popular trail: The Land and Water Conservation Fund helped create the Mountains to Sound Greenway, and expand a recreation area around Mount Si from 4,600 acres to more than 13,000 acres.\" - It's been a really important resource for improving the area around me and protecting it from future development\/pollution.",
//                    "edited": false,
//                    "author_flair_css_class": "Camera",
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;&amp;quot;Mount Si, Washington\u2019s most popular trail: The Land and Water Conservation Fund helped create the Mountains to Sound Greenway, and expand a recreation area around Mount Si from 4,600 acres to more than 13,000 acres.&amp;quot; - It&amp;#39;s been a really important resource for improving the area around me and protecting it from future development\/pollution.&lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cxgsjfs",
//                    "created": 1448839985,
//                    "author_flair_text": null,
//                    "created_utc": 1448811185,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 1
//                }
//                }
//                ],
//                "after": null,
//                "before": null
//            }
//            },
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cx1qijb",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "uscmissinglink",
//                "parent_id": "t1_cvtww3m",
//                "score": 1,
//                "approved_by": null,
//                "controversiality": 1,
//                "body": "This will probably become my most downvoted post of all time (facts aren't welcome on witch hunts) but what the hell.\n\nFor fuck's sake. No.\n\nThe LWCF has exactly fuck to do with *existing* public lands. It's express purpose is to buy and federalize lands currently held privately. There would be no reason whatsoever for the federal government to use federal dollars to buy federal land from itself.\n\nThose lands would only be sold if the owners were willing to sell them - in other words, if the Koch Brothers owned a parcel of land and they wanted to fucking mine the shit out of it while letting their Humvees idle and leaving he refrigerator doors open, there's exactly nothing the LWCF could do about it. Instead, what LWCF does is allow conservation-minded land owners to transfer the financial responsibility of owning land to the public by selling it. People willing to do that are not likely to start strip mining.\n\nMoreover, there are literally billions of dollars in private conservation dollars that seek to do the exact same thing as LWCF. Big private conservation easements are springing up everywhere, rendering the LWCF moot. Oh and by the way contrary to the headline here, the fund *still exists*. It has been running at a surplus and was simply not funded additionally for this fiscal year. Congressional appropriations are annual. If LWCF runs out of funds, it can be re-funded.",
//                "edited": false,
//                "author_flair_css_class": "Camera",
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;This will probably become my most downvoted post of all time (facts aren&amp;#39;t welcome on witch hunts) but what the hell.&lt;\/p&gt;\n\n&lt;p&gt;For fuck&amp;#39;s sake. No.&lt;\/p&gt;\n\n&lt;p&gt;The LWCF has exactly fuck to do with &lt;em&gt;existing&lt;\/em&gt; public lands. It&amp;#39;s express purpose is to buy and federalize lands currently held privately. There would be no reason whatsoever for the federal government to use federal dollars to buy federal land from itself.&lt;\/p&gt;\n\n&lt;p&gt;Those lands would only be sold if the owners were willing to sell them - in other words, if the Koch Brothers owned a parcel of land and they wanted to fucking mine the shit out of it while letting their Humvees idle and leaving he refrigerator doors open, there&amp;#39;s exactly nothing the LWCF could do about it. Instead, what LWCF does is allow conservation-minded land owners to transfer the financial responsibility of owning land to the public by selling it. People willing to do that are not likely to start strip mining.&lt;\/p&gt;\n\n&lt;p&gt;Moreover, there are literally billions of dollars in private conservation dollars that seek to do the exact same thing as LWCF. Big private conservation easements are springing up everywhere, rendering the LWCF moot. Oh and by the way contrary to the headline here, the fund &lt;em&gt;still exists&lt;\/em&gt;. It has been running at a surplus and was simply not funded additionally for this fiscal year. Congressional appropriations are annual. If LWCF runs out of funds, it can be re-funded.&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cx1qijb",
//                "created": 1447676333,
//                "author_flair_text": null,
//                "created_utc": 1447647533,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": 1
//            }
//            },
//            {
//                "kind": "t1",
//                "data": {
//                "subreddit_id": "t5_2sbq3",
//                "banned_by": null,
//                "removal_reason": null,
//                "link_id": "t3_3o2ion",
//                "likes": null,
//                "replies": {
//                "kind": "Listing",
//                "data": {
//                "modhash": "qy491cym5j35a006e51324e2aee58db631bde664e25f162e22",
//                "children": [
//                {
//                    "kind": "t1",
//                    "data": {
//                    "subreddit_id": "t5_2sbq3",
//                    "banned_by": null,
//                    "removal_reason": null,
//                    "link_id": "t3_3o2ion",
//                    "likes": null,
//                    "replies": "",
//                    "user_reports": [
//
//                    ],
//                    "saved": false,
//                    "id": "cwiyokc",
//                    "gilded": 0,
//                    "archived": false,
//                    "report_reasons": null,
//                    "author": "Wilreadit",
//                    "parent_id": "t1_cvv0aiz",
//                    "score": 1,
//                    "approved_by": null,
//                    "controversiality": 0,
//                    "body": "I guess there is an assumption that vegans and vegetarians are more likely to be nature lovers than carnivores.\n\nThis is a biased view propagated by media, where a vegan is a hippie and a nature lover and anti corporate. \n\n",
//                    "edited": false,
//                    "author_flair_css_class": null,
//                    "downs": 0,
//                    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;I guess there is an assumption that vegans and vegetarians are more likely to be nature lovers than carnivores.&lt;\/p&gt;\n\n&lt;p&gt;This is a biased view propagated by media, where a vegan is a hippie and a nature lover and anti corporate. &lt;\/p&gt;\n&lt;\/div&gt;",
//                    "subreddit": "EarthPorn",
//                    "score_hidden": false,
//                    "name": "t1_cwiyokc",
//                    "created": 1446277926,
//                    "author_flair_text": null,
//                    "created_utc": 1446249126,
//                    "distinguished": null,
//                    "mod_reports": [
//
//                    ],
//                    "num_reports": null,
//                    "ups": 1
//                }
//                }
//                ],
//                "after": null,
//                "before": null
//            }
//            },
//                "user_reports": [
//
//                ],
//                "saved": false,
//                "id": "cvv0aiz",
//                "gilded": 0,
//                "archived": false,
//                "report_reasons": null,
//                "author": "I_Has_A_Hat",
//                "parent_id": "t1_cvtww3m",
//                "score": -2,
//                "approved_by": null,
//                "controversiality": 0,
//                "body": "Wait, how do vegans and vegetarians factor into this?",
//                "edited": false,
//                "author_flair_css_class": null,
//                "downs": 0,
//                "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;Wait, how do vegans and vegetarians factor into this?&lt;\/p&gt;\n&lt;\/div&gt;",
//                "subreddit": "EarthPorn",
//                "score_hidden": false,
//                "name": "t1_cvv0aiz",
//                "created": 1444531713,
//                "author_flair_text": null,
//                "created_utc": 1444502913,
//                "distinguished": null,
//                "mod_reports": [
//
//                ],
//                "num_reports": null,
//                "ups": -2
//            }
//            }
//            ],
//            "after": null,
//            "before": null
//        }
//    },
//    "user_reports": [
//
//    ],
//    "saved": false,
//    "id": "cvtww3m",
//    "gilded": 0,
//    "archived": false,
//    "report_reasons": null,
//    "author": "goddamnstupidmofo",
//    "parent_id": "t3_3o2ion",
//    "score": 201,
//    "approved_by": null,
//    "controversiality": 0,
//    "body": "This is a step in the corporate effort to privatize public lands. They're using the \"starve the beast\" approach via the Republicans they've paid for, probably as a flanking attack against Obama's efforts to expand the federal park system and marine sanctuaries. \n\nIt's important to note that this fund is not created with additional taxes on us, but funded by royalties collected from oil companies DRILLING on OUR public lands.  I ask that anyone and everyone, Republican or Democrat or Libertarian or Green, PUSH BACK on this - this affects us all and cuts across party lines. This affects hunters and fishermen, vegans and vegetarians, hikers and equestrians and mountain bikers, mudders and snowmobilers, skiers and snowboarders, photographers and scientists and even documentary watchers who never leave the couch. \n\nUse the link to send a message, but don't stop there - use [this site to find your Representative's phone number and contact them directly outside of wilderness.org - http:\/\/www.house.gov\/representatives\/find\/](http:\/\/www.house.gov\/representatives\/find\/). Don't just make this protest a flood of spam emails from one site - make it loud and clear and individual and angry as hell.\n\nAs someone who deeply cares about our country's natural spaces, and who has spent countless volunteer hours cleaning up my local land trusts and parks (and spends his weekends with his kids teaching them to do the same), I ask you from the bottom of my heart - please help me make them stop this. \n\nOur children deserve to know the beauty of the country we grew up with.",
//    "edited": false,
//    "author_flair_css_class": null,
//    "downs": 0,
//    "body_html": "&lt;div class=\"md\"&gt;&lt;p&gt;This is a step in the corporate effort to privatize public lands. They&amp;#39;re using the &amp;quot;starve the beast&amp;quot; approach via the Republicans they&amp;#39;ve paid for, probably as a flanking attack against Obama&amp;#39;s efforts to expand the federal park system and marine sanctuaries. &lt;\/p&gt;\n\n&lt;p&gt;It&amp;#39;s important to note that this fund is not created with additional taxes on us, but funded by royalties collected from oil companies DRILLING on OUR public lands.  I ask that anyone and everyone, Republican or Democrat or Libertarian or Green, PUSH BACK on this - this affects us all and cuts across party lines. This affects hunters and fishermen, vegans and vegetarians, hikers and equestrians and mountain bikers, mudders and snowmobilers, skiers and snowboarders, photographers and scientists and even documentary watchers who never leave the couch. &lt;\/p&gt;\n\n&lt;p&gt;Use the link to send a message, but don&amp;#39;t stop there - use &lt;a href=\"http:\/\/www.house.gov\/representatives\/find\/\"&gt;this site to find your Representative&amp;#39;s phone number and contact them directly outside of wilderness.org - http:\/\/www.house.gov\/representatives\/find\/&lt;\/a&gt;. Don&amp;#39;t just make this protest a flood of spam emails from one site - make it loud and clear and individual and angry as hell.&lt;\/p&gt;\n\n&lt;p&gt;As someone who deeply cares about our country&amp;#39;s natural spaces, and who has spent countless volunteer hours cleaning up my local land trusts and parks (and spends his weekends with his kids teaching them to do the same), I ask you from the bottom of my heart - please help me make them stop this. &lt;\/p&gt;\n\n&lt;p&gt;Our children deserve to know the beauty of the country we grew up with.&lt;\/p&gt;\n&lt;\/div&gt;",
//    "subreddit": "EarthPorn",
//    "score_hidden": false,
//    "name": "t1_cvtww3m",
//    "created": 1444440940,
//    "author_flair_text": null,
//    "created_utc": 1444412140,
//    "distinguished": null,
//    "mod_reports": [
//
//    ],
//    "num_reports": null,
//    "ups": 201
}