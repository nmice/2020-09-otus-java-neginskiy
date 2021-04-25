package ru.otus.customIocContainer;

import ru.otus.customIocContainer.appcontainer.AppComponentsContainerImpl;
import ru.otus.customIocContainer.appcontainer.api.AppComponentsContainer;
import ru.otus.customIocContainer.config.AppConfig;
import ru.otus.customIocContainer.services.GameProcessor;
import ru.otus.customIocContainer.services.GameProcessorImpl;

/**
 * Neginskiy M.B. 19.04.2021
 * <p>
 * �������� �������
 * ����������� IoC ���������
 * ����:
 * � �������� �������� ������ ��������� ������ ��� �������� �������� ����� Spring framework.
 * <p>
 * ������������ �����:
 * ������� ��������� ���������� ��������� ������� ��������� �� ����������� � ���������
 * � ������ AppComponentsContainerImpl ����������� ���������, ���������� � ������������ ������������,
 * ����������� �� �������� ����������� �� ������ appcontainer. ��� �� ���������� ����������� ������ getAppComponent
 * � ����� ������ ���������� ���������� ����������. ������ ����� ������ ����� AppComponentsContainerImpl
 * �������������� ������� (����� �� ������):
 * <p>
 * ��������� AppConfig �� ��������� ������� � ������������ �� ��� �������� �����������.
 * � AppComponentsContainerImpl �������� �����������, ������� ������������ ��������� �������-������������
 * <p>
 * �������������� ������� (����� �� ������):
 * � AppComponentsContainerImpl �������� �����������, ������� ��������� �� ���� ��� ������,
 * � ������������ ��� ��������� ��� ������-������������ (��. ����������� � pom.xml)
 */
public class App {

    public static void main(String[] args) throws Exception {
        // ������������ ��������
        //AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // ��� ����� ������������ ���������� Reflections (��. �����������)
        //AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");

        // ������������ �������
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // ���������� ������ �������� � ������ �� ��������� ���� ���������
        GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);//TODO �� ����������
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);//TODO �� ����������
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");//TODO �� �����

        gameProcessor.startGame();
    }
}
