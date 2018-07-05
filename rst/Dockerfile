FROM node:8.11-slim as nodeBuilder

ARG NEXUS_URL
ARG NPM_USER

WORKDIR /tmp

ADD rst-web/package*.json /tmp/rst-web/
ADD customization/.npmrc /tmp/rst-web/

RUN  sed -i "s/NEXUS_URL/$NEXUS_URL/g" /tmp/rst-web/.npmrc \
	&& sed -i "s/NPM_USER/$NPM_USER/" /tmp/rst-web/.npmrc


RUN cd rst-web && npm install

ADD rst-web /tmp/rst-web/

RUN cd rst-web && ls &&  ./node_modules/.bin/ng build --environment=dev --aot=false --base-href=/rst/ --target=production

FROM maven:alpine as builder

ARG NEXUS_URL
ARG NEXUS_USER
ARG NEXUS_USER_PASSW
ARG NPM_USER

WORKDIR /tmp

ADD rst-web rst-web/
COPY --from=nodeBuilder /tmp/rst-web/dist rst-web/dist
ADD rst-service rst-service/
ADD rst-app rst-app/
ADD pom.xml .
ADD customization/* customization/

RUN  sed -i "s/NEXUS_URL/$NEXUS_URL/g" /tmp/customization/settings.xml \
	&& sed -i "s/\<NEXUS_USER\>/$NEXUS_USER/" /tmp/customization/settings.xml \
	&& sed -i "s/\<NEXUS_USER_PASSW\>/$NEXUS_USER_PASSW/" /tmp/customization/settings.xml \
	&& mvn -s /tmp/customization/settings.xml -e package -P frontend -Dskip.npm.install=true -Dskip.npm.build=true


RUN mv /tmp/rst-app/target/*.ear /tmp/customization


FROM jboss/wildfly:latest

ENV DBUSER changeme
ENV DBPASSWORD changeme
ENV DBHOST changeme
ENV DBPORT changeme
ENV DBNAME changeme
ENV JNDINAME changeme
ENV MODULE /opt/jboss/wildfly/modules/br/com/ezvida/rst/load/main/
ENV CUSTOMIZATION  /opt/jboss/wildfly/customization

COPY --from=builder /tmp/customization $CUSTOMIZATION/

USER root
RUN chmod +x $CUSTOMIZATION/startup.sh && chown -R jboss: $CUSTOMIZATION/ \
    && curl https://jdbc.postgresql.org/download/postgresql-42.1.4.jar -o $CUSTOMIZATION/postgresql-42.1.4.jar \
    && mkdir -p $MODULE && mkdir -p $MODULE/certificados \
    && echo '<?xml version="1.0" encoding="UTF-8"?> <module xmlns="urn:jboss:module:1.1" name="br.com.ezvida.rst.load"> <resources> <resource-root path="."/> </resources> </module>' > $MODULE/module.xml \
    && echo $'-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7LXgNFtfi2tszl/4+K7P\nL2uI/B+yOqLdwl15p8//xffzk98slN0SIXF8NNwrG9P2ikHXke0U3eZTufzi/BC1\nSxIJd4Ku/nCBQJ6uAT9TuIbqSMJR3cvBrMF/qqMRf+rh09u1p4zFj9DgtYba5OrV\np+xjSFcGAmCpKlC8G8BFya7bn5u04OvW/UHm/xnbp9Q4ecZf92vt8Ma0hoysB0Q/\n6ETG1+g6mRNMNcmtyU+Wvzj1QHOboT14MMLL5b8L3YYf5LCzg7QVNcGtW5g+IJVH\nNv9WQdXTLn+0e98c+1EvyIQu24ZRD5Ra78FYLK0gEiRhwL84hcwXpMMndLtZTHtH\ntwIDAQAB\n-----END PUBLIC KEY-----' > $MODULE/certificados/rsa-public.pem \
    && echo $'-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDsteA0W1+La2zO\nX/j4rs8va4j8H7I6ot3CXXmnz//F9/OT3yyU3RIhcXw03Csb0/aKQdeR7RTd5lO5\n/OL8ELVLEgl3gq7+cIFAnq4BP1O4hupIwlHdy8GswX+qoxF/6uHT27WnjMWP0OC1\nhtrk6tWn7GNIVwYCYKkqULwbwEXJrtufm7Tg69b9Qeb/Gdun1Dh5xl/3a+3wxrSG\njKwHRD/oRMbX6DqZE0w1ya3JT5a/OPVAc5uhPXgwwsvlvwvdhh/ksLODtBU1wa1b\nmD4glUc2/1ZB1dMuf7R73xz7US/IhC7bhlEPlFrvwVgsrSASJGHAvziFzBekwyd0\nu1lMe0e3AgMBAAECggEBAKM87afNl38jDv7nCmAc1T5Db01HYnazWDCwm7PRafaQ\n/oXsMudZ6SXtCBW/+26OZweDTnEQyQIZ04WAUtw3fX6Oqr6i/aDz3v68Lw0nKcoo\nKCsS7rgAysgjDLKVD/1h8mhSrNwl5Rw/lKc6n3ucQskx6Gm7saY0GJTY5lBQ8Q+a\nVbzuyJzn2aKibIx1qmhbYIi6vWmd/yzO25nnkUqPSLaFDADolSj01BJEmn7+Cq3i\nM8RUy5emjFm+TvtUyPM/1Jdl/QznikGoPhTObBK6QbtuPkIOvj1F2+x+fzErbTE4\n1fNntTRcVCYuVtVzttsQA6vq3rTAEF0rp0cu5G8ZVtkCgYEA+zQe3gMysdxSMmgh\n8bDy3TNpian/FJcJ/ngx8Hgv+9DxVsMv+ae3pY/KaCCeWxjYVSsHwFAXPp9Zr+rl\nQVYrbogjheG9IiRo+h1d31nBoXZZu6C6BcN93MKLi3baKbdsuw2HTDQ9ZdoCvaKL\nxmZEpXEUTUYgeEQBn4NuNbFTcdsCgYEA8Trpt1tseCu86BRKcbRe7KCA4lqpEtD/\n7XdNa5shL09L8lO851o9MwWKG0N9d+tAOLqivSyrjs8er+IzapvJa5kiTnmjCATg\nXkttwgD4KD/bWtApjE/bgVoeJlgjUGEYOjbEC0Yr/Yzc6GcfHHJey0KBnd7J7Rja\nSwGqJleNjlUCgYAUqO64965drm8cMjBIfnMa+jV92vtdqCZsRVpmGpxnq4GNzzPM\n5pxWA4N0GOqtN1fjeUyD5pJi1hw8OadtPjcIOi4hvrUdb70qhOvJiMpCC0fKBKgH\n/VIGrAFqEPZUY/+PSseL3ugFzm5XRl0UjiirKqeuy0u0WHOBuP+Bbvob7QKBgACj\n9WblOT6Abzn2g9QVdIMQUgdH0lcYcvWAcZYtjUMA5OS50DCVDsCJ7TnxTe35k+wS\nosf5zGLOrfgtk5Fe5IQCnhKzQ/mLecUGUj5HnmEM+lodX17d/2kygg38Aev+tf/B\nkXeuMgO0UgnT7EM5EoNbo9h1dPfKLxov4PHFHb0ZAoGBAJ0pGAP4NtsNaU7PAvRu\n3O5dCeIh6ZGv7MQfz4x3qyKAOdKQJTDmf9eMQsWmLw7cEY1T2D1L9RjIH8kT3fT1\n0b4WoEHTolfUjsQ961ESJI7QSrCOeNxRUNOpjcPceZB0JhYELjpEQnc+ig2k/1Xz\nCZaCKW+NOcFuEouTaq1rzzXc\n-----END PRIVATE KEY-----' > $MODULE/certificados/rsa-private.pem

USER jboss
CMD ["sh", "-c", "$CUSTOMIZATION/startup.sh"]
