import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.script.*;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private String memory = "";

    public Calculator() {
        setTitle("Scientific Calculator");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        String[] buttons = {
            "sin", "cos", "tan", "√",
            "log", "ln", "^", "π",
            "e", "(", ")", "M+",
            "M-", "MR", "MC", "C",
            "←", "%", "/", "*",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", ".", ""
        };

        JPanel panel = new JPanel(new GridLayout(9, 4, 8, 8));
        for (String b : buttons) {
            if (b.equals("")) {
                panel.add(new JLabel());
            } else {
                JButton btn = new JButton(b);
                btn.setFont(new Font("Arial", Font.BOLD, 22));
                btn.addActionListener(this);
                panel.add(btn);
            }
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {
            case "C" -> display.setText("");
            case "←" -> {
                String text = display.getText();
                if (!text.isEmpty()) {
                    display.setText(text.substring(0, text.length() - 1));
                }
            }
            case "=" -> {
                try {
                    String input = display.getText()
                            .replace("π", String.valueOf(Math.PI))
                            .replace("e", String.valueOf(Math.E))
                            .replace("^", "**");
                    double result = evaluate(input);
                    display.setText(String.valueOf(result));
                } catch (Exception ex) {
                    display.setText("Error");
                }
            }
            case "M+" -> memory = display.getText();
            case "M-" -> memory = "";
            case "MR" -> display.setText(memory);
            case "MC" -> memory = "";
            case "sin", "cos", "tan", "log", "ln", "√" -> applyFunction(cmd);
            case "^" -> display.setText(display.getText() + "^");
            default -> display.setText(display.getText() + cmd);
        }
    }

    private void applyFunction(String func) {
        try {
            double val = evaluate(display.getText());
            double result = switch (func) {
                case "sin" -> Math.sin(Math.toRadians(val));
                case "cos" -> Math.cos(Math.toRadians(val));
                case "tan" -> Math.tan(Math.toRadians(val));
                case "log" -> Math.log10(val);
                case "ln" -> Math.log(val);
                case "√" -> Math.sqrt(val);
                default -> val;
            };
            display.setText(String.valueOf(result));
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    private double evaluate(String expression) throws Exception {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        Object result = engine.eval(expression.replace("**", "^"));
        if (result instanceof Number)
            return ((Number) result).doubleValue();
        else
            throw new Exception("Invalid");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}

