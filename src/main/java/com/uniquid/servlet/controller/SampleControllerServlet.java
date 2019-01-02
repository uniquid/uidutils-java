package com.uniquid.servlet.controller;

import org.eclipse.jetty.http.HttpStatus;

@Controller("/api/v1/sample")
public class SampleControllerServlet extends ControllerServlet {

    public class Test {
        private String text;
        private int num;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }


    @RequestMapping(value = "/{id}/test/{doc}", method = {RequestMethod.POST, RequestMethod.PUT})
    public void test(@RequestBody Test test,
                     @PathVariable("id") int a,
                     @PathParam(value = "time", required = false) Double t,
                     @PathVariable("doc") Long z) {
        System.out.println("Test POST/PUT! " + test.getText());
    }

    @GetMapping("/{id}/test/{dec}")
    public Test test2(@PathVariable("id") int a,
                     @PathParam("time") double t) {
        System.out.println("Test GET! " + a);
        Test ttt = new Test();
        ttt.setNum(99);
        ttt.setText("Bubusette");
        return ttt;
    }

    @GetMapping("/test")
    public ResponseEntity<Test> get() {
        Test t = new Test();
        t.setText("zzz");
        t.setNum(111);
        return new ResponseEntity<>(t, HttpStatus.PROXY_AUTHENTICATION_REQUIRED_407);
    }
}
